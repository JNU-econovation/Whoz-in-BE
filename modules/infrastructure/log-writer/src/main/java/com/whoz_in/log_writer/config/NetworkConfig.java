package com.whoz_in.log_writer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.monitor.MonitorInfo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

//실행시킬 프로세스들이 필요로 하는 정보를 제공하는 역할을 한다.
//이를 위해 network-<profile>.json에서 설정값을 가져오며, 올바른 값인지 검증한다.
@Getter
@Component
public class NetworkConfig {
    private final Map<String, String> ssidInfo; //k: name, v: ssid
    private final MonitorInfo monitorInfo;
    private final List<ManagedInfo> mdnsList;
    private final List<ManagedInfo> arpList;

    @SuppressWarnings("unchecked")
    public NetworkConfig(@Value("${spring.profiles.active:default}") String profile, ResourceLoader loader, ObjectMapper mapper) {
        String jsonPath = "classpath:/network-%s.json".formatted(profile);
        Resource resource = loader.getResource(jsonPath);
        Map<String, Object> map;

        try {
            map = mapper.readValue(resource.getFile(), Map.class); //JSON 파일 읽기
        } catch (IOException e) {
            throw new RuntimeException(jsonPath + " 로드 실패");
        }

        try {
            //interface_info
            List<Map<String, String>> interfaceInfoList = (List<Map<String, String>>) map.get(
                    "ssid_info");
            this.ssidInfo = interfaceInfoList.stream()
                    .collect(Collectors.toMap(info -> info.get("name"), info -> info.get("ssid")));
            //monitor
            Map<String, String> monitorMap = (Map<String, String>) map.get("monitor");
            this.monitorInfo = new MonitorInfo(
                    generateCommand(monitorMap.get("command"), monitorMap.get("interface")));
            // managed
            Map<String, Object> managedMap = (Map<String, Object>) map.get("managed");
            // mdns
            Map<String, Object> mdnsMap = (Map<String, Object>) managedMap.get("mdns");
            String mdnsCommand = (String) mdnsMap.get("command");
            this.mdnsList = ((List<String>) mdnsMap.get("interfaces")).stream()
                    .map(interfaceName -> new ManagedInfo(interfaceName,
                            ssidInfo.get(interfaceName),
                            generateCommand(mdnsCommand, interfaceName)))
                    .toList();
            // arp
            Map<String, Object> arpMap = (Map<String, Object>) managedMap.get("arp");
            String arpCommand = (String) arpMap.get("command");
            this.arpList = ((List<String>) arpMap.get("interfaces")).stream()
                    .map(interfaceName -> new ManagedInfo(interfaceName,
                            ssidInfo.get(interfaceName),
                            generateCommand(arpCommand, interfaceName)))
                    .toList();

            validate();
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("network json 구조가 잘못되었음");
        }
    }

    private String generateCommand(String commandTemplate, String interfaceName) {
        return commandTemplate.replace("{{interface}}", interfaceName);
    }

    private void validate(){
        this.mdnsList.forEach(mdns-> {
                    if (!ssidInfo.containsKey(mdns.interfaceName()))
                        throw new IllegalArgumentException("mdns에 정의되지 않은 인터페이스가 있음");
                });
        this.arpList.forEach(arp-> {
                    if (!ssidInfo.containsKey(arp.interfaceName()))
                        throw new IllegalArgumentException("arp에 정의되지 않은 인터페이스가 있음");
                });
        //TODO: 더 많은 검증
    }


}