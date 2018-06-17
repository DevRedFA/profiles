package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String name);
}
