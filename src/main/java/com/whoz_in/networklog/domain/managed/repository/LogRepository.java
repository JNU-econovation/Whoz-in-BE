package com.whoz_in.networklog.domain.managed.repository;

import com.whoz_in.networklog.infrastructure.jpa.log.NetworkLog;
import java.util.Collection;

public interface LogRepository {

    void saveAll(Collection<NetworkLog> logs);

}
