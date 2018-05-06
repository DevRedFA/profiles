package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByClientId(long clientId);

//    Page<OrderEntity> findAll(Pageable pagable);
}
