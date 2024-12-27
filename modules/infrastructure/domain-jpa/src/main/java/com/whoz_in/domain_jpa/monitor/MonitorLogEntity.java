package com.whoz_in.domain_jpa.monitor;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.whoz_in.domain_jpa.shared.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonitorLogEntity extends BaseEntity {
    @Id
    private String mac;
}
