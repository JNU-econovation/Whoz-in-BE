package com.whoz_in.domain_log_jpa.monitor;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.whoz_in.common_domain_jpa.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MonitorLog extends BaseEntity {
    @Id
    private String mac;
}
