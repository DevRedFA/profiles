package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import com.isedykh.profiles.mapper.ThingMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThingServiceImpl implements ThingService {

    @NonNull
    private ThingEntityRepository thingEntityRepository;

    @NonNull
    private ThingMapper thingMapper;

    @Override
    public List<Thing> getAll() {
        List<Thing> things = thingMapper.ThingEntitiesToThings(thingEntityRepository.findAll());
        return new ArrayList<>(things);
    }

    @Override
    public List<Thing> getAllThingPersonGet(Person person) {
        return null;
    }

    @Override
    public List<Thing> getAllThingPersonGet(long personId) {
        return null;
    }

    @Override
    public Thing saveThing(Thing thing) {
        return null;
    }

    @Override
    public Thing updateThing(Thing thing) {
        return null;
    }

    @Override
    public boolean deleteThing(Thing thing) {
        return false;
    }
}
