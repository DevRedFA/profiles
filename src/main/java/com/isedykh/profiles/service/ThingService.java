package com.isedykh.profiles.service;

import java.util.List;

public interface ThingService {

    List<Thing> findAll();

    Thing getById(long id);

    List<Thing> getAllThingsByType(ThingType type);

    List<Thing> getAllThingPersonGet(Client client);

    List<Thing> getAllThingPersonGet(long personId);

    Thing saveThing(Thing thing);

    Thing updateThing(Thing thing);

    boolean deleteThing(Thing thing);

}
