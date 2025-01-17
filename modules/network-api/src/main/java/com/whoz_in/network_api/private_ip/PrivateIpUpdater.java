package com.whoz_in.network_api.private_ip;

import com.whoz_in.network_api.common.NetworkInterface;
import com.whoz_in.network_api.config.NetworkConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//주기적으로 자신의 내부 아이피를 알아내고 PrivateIpWriter를 이용하여 main-api에 저장
@Slf4j
@Component
@RequiredArgsConstructor
public class PrivateIpUpdater {
    private final NetworkConfig networkConfig;
    private final PrivateIpWriter writer;

    @Scheduled(fixedRate = 30000)
    private void update() {
        Map<String, String> privateIp = getPrivateIpPerSsid();
        writer.write(networkConfig.getRoom(), privateIp);
        log.info("[private ip] updated");
    }

    private Map<String, String> getPrivateIpPerSsid(){
        List<NetworkInterface> networkInterfaces = networkConfig.getManagedNIs();
        //아이피를 얻지 못했을 경우 담지 않는다.
        Map<String, String> privateIps = new HashMap<>();
        for (NetworkInterface ni : networkInterfaces) {
            SystemPrivateIpResolver.getIPv4(ni.getInterfaceName())
                    .ifPresent(ip->privateIps.put(ni.getAltSsid(), ip));
        }
        return privateIps;
    }
}
