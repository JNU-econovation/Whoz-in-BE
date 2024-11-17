package com.whoz_in.networklog.domain.log.model;

public enum ProcessableProtocol {

    MDNS("mdns"),
    ICMP("icmp"),
    ARP("arp");

    private final String protocol;

    ProcessableProtocol(String protocol) {
        this.protocol = protocol;
    }

}
