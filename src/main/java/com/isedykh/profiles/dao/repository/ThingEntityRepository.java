package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingEntityRepository extends JpaRepository<ThingEntity, Long> {

    List<ThingEntity> findAllByType(ThingTypeEntity type);
}
