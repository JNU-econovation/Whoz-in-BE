package com.whoz_in.api_query_jpa.device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Getter
@Subselect("SELECT d.id , d.member_id, d.name "
        + "FROM device_entity d")
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name ="member_id", nullable = false)
    private UUID memberId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private List<DeviceInfo> deviceInfos;

}
