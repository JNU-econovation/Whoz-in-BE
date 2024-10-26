package com.whoz_in.domain_jpa.device;

import com.whoz_in.domain_jpa.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DeviceEntity extends BaseEntity {
    @Id
    private Long deviceId;

}
