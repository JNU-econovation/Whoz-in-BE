package com.whoz_in.network_api.managed;


import com.whoz_in.network_api.common.network_interface.NetworkInterface;

//Managed 프로세스를 생성하고 처리하는 과정에서 필요한 정보를 담는다.
public record ManagedInfo(NetworkInterface ni, String command) {}
