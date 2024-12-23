package com.whoz_in.network_api.common.util;

import org.springframework.stereotype.Component;

@Component
public class IpHolder implements RequesterInfo{

    private final ThreadLocal<String> ip = new ThreadLocal<>();

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public String getIp(){
        return ip.get();
    }

    public void clear(){
        ip.remove();
    }

}
