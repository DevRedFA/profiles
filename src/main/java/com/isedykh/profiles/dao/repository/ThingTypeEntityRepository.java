package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingTypeEntityRepository extends JpaRepository<ThingTypeEntity, Long> {
}
