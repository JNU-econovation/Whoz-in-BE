package com.whoz_in.network_api.controller;

import com.whoz_in.network_api.common.util.IpHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class NetworkApiController {
    private final IpHolder ipHolder;
    private final String room;

    public NetworkApiController(IpHolder ipHolder, @Value("${room-setting.room-name}") String room) {
        this.ipHolder = ipHolder;
        this.room = room;
    }

    @GetMapping("/ip")
    public ResponseEntity<String> getIp() {
        String ip = ipHolder.getIp();
        log.info("Requester Info : " + ip);
        return ResponseEntity.ok(ip);
    }

    @GetMapping("/room")
    public ResponseEntity<String> getRoom(){
        return ResponseEntity.ok(room);
    }
}
