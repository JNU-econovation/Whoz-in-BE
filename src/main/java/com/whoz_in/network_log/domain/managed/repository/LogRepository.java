package com.whoz_in.network_log.domain.managed.repository;

import com.whoz_in.network_log.infrastructure.jpa.log.NetworkLog;
import java.util.Collection;

public interface LogRepository {

    void saveAll(Collection<NetworkLog> logs);

}
