package com.nuttu.aicloud.repository;

import com.nuttu.aicloud.model.device.Device;
import com.nuttu.aicloud.model.gateway.DeviceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceConfigRepository extends JpaRepository<DeviceConfig, String> {
    Optional<DeviceConfig> findOneById(@Param("id") Integer id);
}
