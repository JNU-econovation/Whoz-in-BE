package com.whoz_in.network_log.application.log.adaptor;

import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import com.whoz_in.network_log.infrastructure.jpa.log.LogJpaRepository;
import com.whoz_in.network_log.infrastructure.jpa.log.NetworkLog;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogAdaptor implements LogRepository {
    
    private final LogJpaRepository logJpaRepository;

    @Override
    public void saveAll(Collection<NetworkLog> logs) {
        logJpaRepository.saveAll(logs);
    }
}
