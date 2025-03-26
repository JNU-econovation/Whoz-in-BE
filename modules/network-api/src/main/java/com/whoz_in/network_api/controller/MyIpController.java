package com.whoz_in.network_api.controller;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.*;
import static com.whoz_in.network_api.common.network_interface.WirelessMode.MANAGED;

import com.whoz_in.network_api.common.network_interface.NetworkAddress;
import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.util.IpHolder;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.controller.docs.MyIpApi;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class MyIpController implements MyIpApi {
    private final NetworkInterfaceProfileConfig profileConfig;
    private final NetworkInterfaceManager manager;
    private final IpHolder ipHolder;
    private Set<String> gateways;
    private final List<String> hosts;

    public MyIpController(
            IpHolder ipHolder,
            NetworkInterfaceProfileConfig profileConfig,
            NetworkInterfaceManager manager,
            @Value("${my-ip-hosts}") List<String> hosts
            ) {
        this.profileConfig = profileConfig;
        this.manager = manager;
        this.ipHolder = ipHolder;
        this.gateways = renewGateways();
        this.hosts = hosts;
    }

    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        if (event.status() == RECONNECTED || event.status() == ADDED_AND_RECONNECTED) {
            if (!manager.available(MANAGED)) return;
            this.gateways = renewGateways();
        }
    }

    private Set<String> renewGateways(){
        return profileConfig.getManagedProfiles().stream()
                .map(profile -> manager.get().get(profile.interfaceName()))
                .map(NetworkInterface::getNetworkAddress)
                .map(NetworkAddress::gateway) // 시작 시 검증되기에 이상적으론 여기서 NPE 뜰 수 없음
                .collect(Collectors.toSet());
    }

    @Override
    @GetMapping("/my-ip")
    public ResponseEntity<String> getIp() throws UnknownHostException {
        if (!manager.available(MANAGED))
            return ResponseEntity.internalServerError().body("동아리방 서버의 와이파이가 불안정합니다.");
        String ip = ipHolder.getIp();
        log.info("Requester Info : " + ip);
        if (gateways.contains(ip) || !InetAddress.getByName(ip).isSiteLocalAddress()){ // 루프백도 외부 아이피로 간주된다.
            return ResponseEntity.badRequest().body("외부 아이피로 요청됨");
        }
        return ResponseEntity.ok(ip);
    }

    @Override
    @GetMapping("/my-ip-hosts")
    public ResponseEntity<List<String>> getAccessIps(){
        return ResponseEntity.ok(this.hosts);
    }
}
