package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ThingTypeService extends CrudService<ThingType> {

    List<ThingType> findAll();

    Page<ThingType> findAll(Pageable pageable);

    ThingType save(ThingType thingType);

    void delete(ThingType thingType);

    ThingType findById(long id);
}
