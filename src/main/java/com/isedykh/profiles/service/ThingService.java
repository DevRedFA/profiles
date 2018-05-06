package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ThingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ThingService {

    List<Thing> findAll();

    Thing getById(long id);

    List<Thing> getAllThingsByType(ThingType type);

    List<Thing> getAllThingPersonGet(Client client);

    List<Thing> getAllThingPersonGet(long personId);

    Page<Thing> findAll(Pageable pageable);

    Page<ThingDto> findAllToDto(Pageable pageable);

    Thing saveThing(Thing thing);

    Thing updateThing(Thing thing);

    boolean deleteThing(Thing thing);

}
