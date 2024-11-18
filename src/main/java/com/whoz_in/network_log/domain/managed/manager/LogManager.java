package com.whoz_in.network_log.domain.managed.manager;

import java.util.Set;

public interface LogManager {

    public void receive(Set<String> logs);
    
    public void receive(String log);

}
