package com.nuttu.aicloud.repository;

import com.nuttu.aicloud.model.fileNameList.fileNameList;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FileNameListRepository extends JpaRepository<fileNameList, String> {
    boolean existsByfileName(String fileName);
    boolean existsById(Integer id);
    Page<fileNameList> findAll(Pageable pageable);
    Page<fileNameList> findAll(Specification<fileNameList> spec,Pageable pageable);

//    @Modifying
//    @Query(value = "delete from fileNameList f where f.id =?1")
//    void deleteById(Integer id);

    Optional<fileNameList> findOneByFileName(String fileName);
    Optional<fileNameList> findOneById(Integer id);

}
