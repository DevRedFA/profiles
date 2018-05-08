package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<T> {

    Page<T> findAll(Pageable pageable);

    void delete(T t);

}