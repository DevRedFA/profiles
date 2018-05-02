package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonEntityRepository extends JpaRepository<PersonEntity, Long> {

    List<PersonEntity> findAllByName(String name);

    List<PersonEntity> findAllByPhoneOrPhoneSecond(long phone, long phoneSecond);
}
