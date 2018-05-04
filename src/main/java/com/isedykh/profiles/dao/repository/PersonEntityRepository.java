package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonEntityRepository extends JpaRepository<ClientEntity, Long> {

    List<ClientEntity> findAllByName(String name);

    List<ClientEntity> findAllByPhoneOrPhoneSecond(long phone, long phoneSecond);
}
