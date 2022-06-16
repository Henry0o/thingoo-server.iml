package com.nuttu.aicloud.repository;

import com.nuttu.aicloud.model.gateway.HealthCode;
import com.nuttu.aicloud.model.gateway.HealthIndoModel;
import com.nuttu.aicloud.model.gateway.HealthInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author wing
 */
public interface HealthInfoRepository extends JpaRepository<HealthInfo,String> {
    boolean existsBySn(String sn);
    @Query(value = "SELECT new com.nuttu.aicloud.model.gateway.HealthIndoModel(h.uuid,h.sn,h.version,h.owner,h.type,h.id_card,h.name,h.phone,h.code_color,h.photo,h.photo_time,h.pcr_igmc,h.pcr_time,h.pcr_result,h.vacc,h.vacc_date,h.qr_show,h.qr_create,h.trip,h.qr_content) FROM HealthInfo h WHERE h.uuid=?1")
    Optional<HealthIndoModel> findOneByUuid(String uuid);
    Page<HealthInfo> findByOwner(String owner, Pageable pageable);
    @Query(value = "SELECT new com.nuttu.aicloud.model.gateway.HealthIndoModel(h.uuid,h.owner,g.address,h.version,h.sn,h.type,h.name,h.code_color,h.pcr_result) FROM HealthInfo h, Gateway g WHERE h.sn=g.mac AND h.owner=?1")
    Page<HealthIndoModel> findPartByOwner(String owner, Pageable pageable);
    @Query(value = "SELECT new com.nuttu.aicloud.model.gateway.HealthIndoModel(h.uuid,h.owner,g.address,h.version,h.sn,h.type,h.name,h.code_color,h.pcr_result) FROM HealthInfo h, Gateway g WHERE h.sn=g.mac AND h.owner LIKE ?1 AND h.sn LIKE ?2 AND h.code_color LIKE ?3 AND h.type LIKE ?4")
    Page<HealthIndoModel> findPartByParams(String owner, String sn, String code_color, String type, Pageable pageable);
    List<HealthInfo> findAllByOwner(String userId);
    List<HealthInfo> findAllByOwnerAndSnIn(String userId, Collection<String> sns);
    Page<HealthInfo> findAll(Pageable pageable);
    @Query(value = "SELECT new com.nuttu.aicloud.model.gateway.HealthIndoModel(h.uuid,h.owner,g.address,h.version,h.sn,h.type,h.name,h.code_color,h.pcr_result) FROM HealthInfo h, Gateway g WHERE h.sn=g.mac")
    Page<HealthIndoModel> findAllPart(Pageable pageable);
    Optional<HealthInfo> findOneBySn(String sn);
    List<HealthInfo> findBySn(String sn);

}
