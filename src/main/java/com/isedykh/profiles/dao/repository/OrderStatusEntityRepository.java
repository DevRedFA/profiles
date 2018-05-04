package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusEntityRepository extends JpaRepository<OrderStatusEntity, Long> {

}
