package com.whoz_in.network_log.infra.managed.mdns.manager;

import java.util.Set;

public interface LogManager {

    public void receive(Set<String> logs);
    
    public void receive(String log);

}
