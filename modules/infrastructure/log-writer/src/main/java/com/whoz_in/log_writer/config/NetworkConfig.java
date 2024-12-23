package com.whoz_in.log_writer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whoz_in.log_writer.common.NetworkInterface;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.monitor.MonitorInfo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

//실행시킬 프로세스들이 필요로 하는 정보를 제공하는 역할을 한다.
//이를 위해 network-<profile>.json에서 설정값을 가져온다.
//local 혹은 prod만 지원한다.
@Getter
@Component
public class NetworkConfig {
    private final List<NetworkInterface> networkInterfaces;
    private final MonitorInfo monitorInfo;
    private final List<ManagedInfo> mdnsList;
    private final List<ManagedInfo> arpList;

    @SuppressWarnings("unchecked")
    public NetworkConfig(@Value("${spring.profiles.active}") String profile, ResourceLoader loader, ObjectMapper mapper) {
        String jsonPath = "classpath:/network-%s.json".formatted(profile);
        Resource resource = loader.getResource(jsonPath);
        Map<String, Object> map;

        //JSON 파일 읽기
        try {
            map = mapper.readValue(resource.getInputStream(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(jsonPath + " 로드 실패");
        }

        //매핑한 map으로부터 값을 추출
        //interface_info
        List<Map<String, String>> interfaceInfoList = (List<Map<String, String>>) map.get(
                "network_interfaces");
        this.networkInterfaces = interfaceInfoList.stream()
                .map(info->new NetworkInterface(info.get("name"), info.get("ssid"), info.get("mode")))
                .toList();
        //monitor
        Map<String, String> monitorMap = (Map<String, String>) map.get("monitor");
        this.monitorInfo = new MonitorInfo(
                this.networkInterfaces.stream()
                        .filter(ni->ni.getName().equals(monitorMap.get("interface")))
                        .findAny()
                        .orElseThrow(()->new IllegalStateException(monitorMap.get("interface")+"은 설정된 network_interfaces에 존재하지 않습니다.")),
                generateCommand(monitorMap.get("command"), monitorMap.get("interface"))
        );
        // managed
        Map<String, Object> managedMap = (Map<String, Object>) map.get("managed");
        // mdns
        Map<String, Object> mdnsMap = (Map<String, Object>) managedMap.get("mdns");
        String mdnsCommand = (String) mdnsMap.get("command");
        this.mdnsList = ((List<String>) mdnsMap.get("interfaces")).stream()
                .map(interfaceName -> {
                    NetworkInterface mdnsNI = networkInterfaces.stream()
                            .filter(ni -> ni.getName().equals(interfaceName))
                            .findAny()
                            .orElseThrow(()->new IllegalStateException(interfaceName+"은 설정된 network_interfaces에 존재하지 않습니다."));
                    return new ManagedInfo(mdnsNI, generateCommand(mdnsCommand, interfaceName));
                })
                .toList();
        // arp
        Map<String, Object> arpMap = (Map<String, Object>) managedMap.get("arp");
        String arpCommand = (String) arpMap.get("command");
        this.arpList = ((List<String>) arpMap.get("interfaces")).stream()
                .map(interfaceName -> {
                    NetworkInterface arpNI = networkInterfaces.stream().filter(
                            ni -> ni.getName().equals(interfaceName)
                    ).findAny().orElseThrow(()->new IllegalStateException(interfaceName+"은 설정된 network_interfaces에 존재하지 않습니다."));
                    return new ManagedInfo(arpNI, generateCommand(arpCommand, interfaceName));
                })
                .toList();
    }

    private String generateCommand(String commandTemplate, String interfaceName) {
        return commandTemplate.replace("{{interface}}", interfaceName);
    }

}