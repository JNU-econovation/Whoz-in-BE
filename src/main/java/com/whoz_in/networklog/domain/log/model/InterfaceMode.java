package com.whoz_in.networklog.domain.log.model;

public enum InterfaceMode {

    MONITOR("monitor"),
    MANAGED("managed");

    private String mode;

    InterfaceMode(String mode) {
        this.mode = mode;
    }


}
