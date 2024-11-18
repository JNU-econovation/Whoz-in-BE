package com.whoz_in.network_log.domain.managed.repository;

import com.whoz_in.network_log.domain.managed.ManagedLog;
import java.util.Collection;

public interface LogRepository {

    void saveAll(Collection<ManagedLog> logs);

    void bulkInsert(Collection<ManagedLog> logs);

    void save(ManagedLog log);

}
