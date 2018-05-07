package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThingDtoService extends PageableService<ThingDto> {

    Page<ThingDto> findAll(Pageable pageable);
}
