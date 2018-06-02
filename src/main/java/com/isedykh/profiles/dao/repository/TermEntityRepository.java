package com.isedykh.profiles.dao.repository;

import com.isedykh.profiles.dao.entity.TermEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermEntityRepository extends JpaRepository<TermEntity, Long> {
}
