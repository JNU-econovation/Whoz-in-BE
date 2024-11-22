package com.whoz_in.network_log.infra.managed.mdns;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
@Builder
public class LogDTO {

    private final String mac;
    private final String ip;
    private final String device;
    
    public static LogDTO createNew(String mac, String ip, String device) {
        return LogDTO.builder().mac(mac).ip(ip).device(device).build();
    }

}
