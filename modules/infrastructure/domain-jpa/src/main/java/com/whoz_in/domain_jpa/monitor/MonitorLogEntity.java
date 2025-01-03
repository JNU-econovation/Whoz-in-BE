package com.whoz_in.domain_jpa.monitor;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.whoz_in.domain_jpa.shared.BaseEntity;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonitorLogEntity extends BaseEntity {
    @Id
    private String mac;
    @Column(nullable = false)
    private String room;
}
