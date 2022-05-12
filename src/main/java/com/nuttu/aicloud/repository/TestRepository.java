package com.nuttu.aicloud.repository;

import com.nuttu.aicloud.model.device.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Device,String> {
    Optional<Device> findOneByUuid(String uuid);
}
