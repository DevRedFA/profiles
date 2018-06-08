package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudService<T> {

    Page<T> findAll(Pageable pageable);

    void delete(T t);

    T save(T t);

    void delete(long id);
}
