package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.ThingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingEntityRepository extends JpaRepository<ThingEntity, Long> {
}
