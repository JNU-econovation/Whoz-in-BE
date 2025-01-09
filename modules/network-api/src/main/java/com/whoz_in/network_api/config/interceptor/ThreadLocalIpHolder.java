package com.whoz_in.network_api.config.interceptor;

import com.whoz_in.network_api.common.util.IpHolder;
import org.springframework.stereotype.Component;

//ThreadLocal로 구현
@Component
public class ThreadLocalIpHolder implements IpHolder {

    private static final ThreadLocal<String> ip = new ThreadLocal<>();

    public void setIp(String ip) {
        ThreadLocalIpHolder.ip.set(ip);
    }

    @Override
    public String getIp(){
        return ip.get();
    }

    public void clear(){
        ip.remove();
    }

}
