package com.whoz_in.network_log.domain.monitor;

import com.whoz_in.network_log.infrastructure.jpa.log.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MonitorLog extends BaseEntity {
    @Id
    private String mac;
}
