package com.nuttu.aicloud.repository;

import com.nuttu.aicloud.model.device.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {
    Optional<Device> findOneByUuid(String uuid);
    Page<Device> findByUserId(String userId, Pageable pageable);
    List<Device> findByUserIdAndMacIn(String userId, Collection<String> macs);
    Page<Device> findAll(Specification<Device> spec, Pageable pageable);
    Optional<Device> findOneByMac(String mac);
}
