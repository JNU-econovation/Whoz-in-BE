package com.whoz_in.network_log.domain.managed.repository;

import com.whoz_in.network_log.domain.managed.LogDTO;
import com.whoz_in.network_log.domain.managed.ManagedLog;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LogRepository {

    void saveAll(Collection<ManagedLog> logs);

    void bulkInsert(Collection<ManagedLog> logs);

    void save(ManagedLog log);

    //제일 최근의 맥을 가져옴
    ManagedLog findByIp(String ip);

    List<ManagedLog> findAllByIp(String ip);

}
