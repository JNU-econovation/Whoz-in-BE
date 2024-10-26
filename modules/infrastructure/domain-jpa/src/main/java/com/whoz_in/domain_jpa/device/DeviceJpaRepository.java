package com.whoz_in.domain_jpa.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceJpaRepository extends JpaRepository<DeviceEntity, Long> {

}