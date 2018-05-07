package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThingDtoService extends CrudService<ThingDto> {

    Page<ThingDto> findAll(Pageable pageable);
}
