package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.OrderTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTypeEntityRepository extends JpaRepository<OrderTypeEntity, Long> {
}
