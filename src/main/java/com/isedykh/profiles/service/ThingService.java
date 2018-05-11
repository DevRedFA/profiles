package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ThingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ThingService extends CrudService<Thing> {

    List<Thing> findAll();

    Thing getById(long id);

    List<Thing> getAllThingsByType(ThingType type);

    List<Thing> getAllThingsByTypeFreeBetween(ThingType type, LocalDate start, LocalDate to);

    List<Thing> getAllThingPersonGet(Client client);

    List<Thing> findAllByName(String name);

    List<Thing> getAllThingPersonGet(long personId);

    Page<Thing> findAll(Pageable pageable);

    Page<ThingDto> findAllToDto(Pageable pageable);

    Thing save(Thing thing);

    Thing update(Thing thing);
}
