package com.whoz_in.network_api.controller;

import com.whoz_in.network_api.common.network_interface.NetworkAddress;
import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status;
import com.whoz_in.network_api.common.util.IpHolder;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.controller.docs.NetworkApi;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
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
public class NetworkApiController implements NetworkApi {
    private final NetworkInterfaceProfileConfig profileConfig;
    private final NetworkInterfaceManager manager;
    private final IpHolder ipHolder;
    private Set<String> gateways;
    private final String room;
    private final List<String> disconnectedNIs;

    public NetworkApiController(
            IpHolder ipHolder,
            @Value("${room-setting.room-name}") String room,
            NetworkInterfaceProfileConfig profileConfig,
            NetworkInterfaceManager manager) {
        this.profileConfig = profileConfig;
        this.manager = manager;
        this.ipHolder = ipHolder;
        this.room = room;
        this.disconnectedNIs = new CopyOnWriteArrayList<>();
        this.gateways = getGateways();
    }

    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        if (event.status() == Status.DISCONNECTED || event.status() == Status.REMOVED) {
            String disconnectedNI = event.pre().getName();
            disconnectedNIs.add(disconnectedNI);
            log.warn("{}의 네트워크 연결이 끊겨 ip 반환 컨트롤러가 비활성화됨", disconnectedNI);
        }
        if (event.status() == Status.RECONNECTED || event.status() == Status.ADDED_AND_RECONNECTED) {
            disconnectedNIs.remove(event.interfaceName());
            if (!disconnectedNIs.isEmpty()) return;
            this.gateways = getGateways();
            log.info("ip 반환 컨트롤러 정상화됨");
        }
    }

    private Set<String> getGateways(){
        return profileConfig.getManagedProfiles().stream()
                .map(profile -> manager.get().get(profile.interfaceName()))
                .map(NetworkInterface::getNetworkAddress)
                .map(NetworkAddress::gateway) // 시작 시 검증되기에 이상적으론 여기서 NPE 뜰 수 없음
                .collect(Collectors.toSet());
    }

    @GetMapping("/ip")
    public ResponseEntity<String> getIp() throws UnknownHostException {
        if (!this.disconnectedNIs.isEmpty())
            return ResponseEntity.internalServerError().body("서버가 와이파이에 연결되지 않음");
        String ip = ipHolder.getIp();
        log.info("Requester Info : " + ip);
        if (gateways.contains(ip) || !InetAddress.getByName(ip).isSiteLocalAddress()){ // 루프백도 외부 아이피로 간주된다.
            return ResponseEntity.badRequest().body("외부 아이피로 요청됨");
        }
        return ResponseEntity.ok(ip);
    }

    @GetMapping("/room")
    public ResponseEntity<String> getRoom(){
        return ResponseEntity.ok(room);
    }
}
