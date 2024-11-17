package com.whoz_in.network_log.domain.managed.model;

public enum InterfaceMode {

    MONITOR("monitor"),
    MANAGED("managed");

    private String mode;

    InterfaceMode(String mode) {
        this.mode = mode;
    }


}
