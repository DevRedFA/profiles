package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ThingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ThingDtoService extends CrudService<ThingDto> {

    Page<ThingDto> findAll(Pageable pageable);

    List<ThingDto> getAllThingsByTypeFreeBetween(ThingType type, LocalDate begin, LocalDate end);
}
