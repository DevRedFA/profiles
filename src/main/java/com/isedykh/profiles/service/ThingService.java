package com.isedykh.profiles.service;

import java.util.List;

public interface ThingService {

    List<Thing> getAll();

    List<Thing> getAllThingPersonGet(Person person);

    List<Thing> getAllThingPersonGet(long personId);

    Thing saveThing(Thing thing);

    Thing updateThing(Thing thing);

    boolean deleteThing(Thing thing);

}
