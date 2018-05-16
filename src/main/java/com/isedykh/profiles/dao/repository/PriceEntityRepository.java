package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceEntityRepository extends JpaRepository<PriceEntity, Long> {
}
