package com.whoz_in.network_api.monitor;


import com.whoz_in.network_api.common.network_interface.NetworkInterface;

//Monitor 프로세스 생성자에서 요구하는 객체로, 프로세스를 생성하고 처리하기 위해 필요한 정보를 담는다.
public record MonitorInfo(
        NetworkInterface ni,
        String command
) {}