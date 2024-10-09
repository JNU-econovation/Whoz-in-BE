package com.whoz_in.infra.jpa.device;

import com.whoz_in.infra.jpa.shared.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "deleted_at is null")
@Table(name = "device")
@SQLDelete(sql = "UPDATE device SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class DeviceEntity extends BaseEntity {

}
