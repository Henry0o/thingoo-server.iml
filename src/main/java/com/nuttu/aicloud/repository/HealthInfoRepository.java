package com.nuttu.aicloud.repository;

import com.nuttu.aicloud.model.gateway.HealthCode;
import com.nuttu.aicloud.model.gateway.HealthInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author wing
 */
public interface HealthInfoRepository extends JpaRepository<HealthInfo,String> {
    boolean existsBySn(String sn);
    Optional<HealthInfo> findOneByUuid(String uuid);
    Page<HealthInfo> findByOwner(String owner, Pageable pageable);
    List<HealthInfo> findAllByOwner(String userId);
    List<HealthInfo> findAllByOwnerAndSnIn(String userId, Collection<String> sns);
    Page<HealthInfo> findAll(Pageable pageable);
    Optional<HealthInfo> findOneBySn(String sn);
    List<HealthInfo> findBySn(String sn);

}
