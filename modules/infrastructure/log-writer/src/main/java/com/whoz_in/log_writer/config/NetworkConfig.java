package com.whoz_in.log_writer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NetworkConfig {
    // Getters and setters
    private final Map<String, String> ssidInfo; //k: name, v: ssid
    private final Monitor monitor;
    private final List<Managed> mdnsList;
    private final List<Managed> arpList;

    @SuppressWarnings("unchecked")
    public NetworkConfig(@Value("${spring.profiles.active:default}") String profile, ResourceLoader loader, ObjectMapper mapper) {
        Resource resource = loader.getResource("classpath:/network-%s.json".formatted(profile));

        try {
            //JSON 파일 읽기
            Map<String, Object> map = mapper.readValue(resource.getFile(), Map.class);

            //interface_info
            List<Map<String, String>> interfaceInfoList = (List<Map<String, String>>) map.get("ssid_info");
            this.ssidInfo = interfaceInfoList.stream()
                    .collect(Collectors.toMap(info-> info.get("name"), info->info.get("ssid")));

            //monitor
            Map<String, String> monitorMap = (Map<String, String>) map.get("monitor");
            this.monitor = new Monitor(
                    monitorMap.get("interface"),
                    generateCommand(monitorMap.get("command"), monitorMap.get("interface"))
            );

            // managed
            Map<String, Object> managedMap = (Map<String, Object>) map.get("managed");

            // mdns
            Map<String, Object> mdnsMap = (Map<String, Object>) managedMap.get("mdns");
            String mdnsCommand = (String) mdnsMap.get("command");
            this.mdnsList = ((List<String>) mdnsMap.get("interfaces")).stream()
                    .map(interfaceName->new Managed(interfaceName, ssidInfo.get(interfaceName), generateCommand(mdnsCommand, interfaceName)))
                    .toList();

            // arp
            Map<String, Object> arpMap = (Map<String, Object>) managedMap.get("arp");
            String arpCommand = (String) arpMap.get("command");
            this.arpList = ((List<String>) arpMap.get("interfaces")).stream()
                    .map(interfaceName->new Managed(interfaceName, ssidInfo.get(interfaceName), generateCommand(arpCommand, interfaceName)))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration from JSON file", e);
        }
    }

    private String generateCommand(String commandTemplate, String interfaceName) {
        return commandTemplate.replace("{{interface}}", interfaceName);
    }

    public record Monitor(String interfaceName, String command) {}
    public record Managed(String interfaceName, String ssid, String command) {}
}