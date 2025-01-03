package com.whoz_in.domain_jpa.managed;

import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ManagedLogJpaRepository implements ManagedLogRepository {
    private final ManagedLogEntityRepository  managedLogEntityRepository;
    private final ManagedLogConverter managedLogConverter;
    @Override
    public void save(ManagedLog log) {
        managedLogEntityRepository.save(managedLogConverter.from(log));
    }

}
