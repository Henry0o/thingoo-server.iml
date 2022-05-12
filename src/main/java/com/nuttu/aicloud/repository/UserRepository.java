package com.nuttu.aicloud.repository;


import com.nuttu.aicloud.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findAll(Pageable pageable);
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Optional<User> findOneByUuid(String uuid);
    Optional<User> findOneByEmail(String email);

    //@Query("select s from user s where s.email=?1 or s.mobile_phone=?1")    //未调试，暂时没用
    //Optional<User> findOneByLogin(String login);    //可以是email或者phone number

    Optional<User> findOneByUuidAndPassword(String uuid, String password);
    Optional<User> findOneByEmailAndPassword(String email, String password);

}

