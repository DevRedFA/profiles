package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findByUserId(Long userId);
}
