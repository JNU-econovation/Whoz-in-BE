package com.whoz_in.log_writer.config;

import com.whoz_in.log_writer.common.NetworkInterface;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.monitor.MonitorInfo;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//실행시킬 프로세스들이 필요로 하는 정보를 제공하는 역할을 한다.
//다른 모듈이 구현한 LogWriterConfig를 통해 정보를 초기화한다.
@Getter
@Component
public class NetworkConfig {
    private final List<NetworkInterface> networkInterfaces;
    private final MonitorInfo monitorInfo;
    private final List<ManagedInfo> mdnsList;
    private final List<ManagedInfo> arpList;

    public NetworkConfig(
            LogWriterConfig config,
            @Value("${command.monitor}") String monitorCommandTemplate,
            @Value("${command.managed.mdns}") String mdnsCommandTemplate,
            @Value("${command.managed.arp}") String arpCommandTemplate
    ) {
        this.networkInterfaces = config.getNIs();
        this.monitorInfo = new MonitorInfo(config.getMonitorNI(), generateCommand(monitorCommandTemplate, config.getMonitorNI().getName()));
        this.mdnsList=config.getMdnsNIs().stream()
                .map(ni-> new ManagedInfo(ni, generateCommand(mdnsCommandTemplate, ni.getName())))
                .toList();
        this.arpList=config.getArpNIs().stream()
                .map(ni-> new ManagedInfo(ni, generateCommand(arpCommandTemplate, ni.getName())))
                .toList();
    }

    private String generateCommand(String commandTemplate, String interfaceName) {
        return commandTemplate.replace("{{interface}}", interfaceName);
    }

}