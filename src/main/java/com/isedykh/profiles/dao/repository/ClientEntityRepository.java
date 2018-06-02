package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientEntityRepository extends JpaRepository<ClientEntity, Long> {

//    @Query("select u from client u where u.NAME like %?1")
    List<ClientEntity> findAllByNameContaining(String name);

    List<ClientEntity> findAllByPhoneOrPhoneSecond(long phone, long phoneSecond);

//    List<ClientEntity> findAllByOrdersIdIn(List<Long> orderIds);

//    ClientEntity findByOrdersIdIn(Long orderId);
}
