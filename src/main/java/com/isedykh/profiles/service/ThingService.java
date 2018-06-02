package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.service.entity.Thing;
import com.isedykh.profiles.service.entity.ThingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ThingService extends CrudService<Thing> {

    List<Thing> findAll();

    Thing getById(long id);

    List<Thing> getAllThingsByType(ThingTypeEntity type);

    List<Thing> getAllThingsByTypeFreeBetween(ThingType type, LocalDate start, LocalDate to);

    Page<Thing> findAll(Pageable pageable);

    Thing save(Thing thing);
}
