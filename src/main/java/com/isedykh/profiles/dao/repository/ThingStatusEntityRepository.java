package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.ThingStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingStatusEntityRepository extends JpaRepository<ThingStatusEntity, Long> {

    ThingStatusEntity findByName(String name);
}
