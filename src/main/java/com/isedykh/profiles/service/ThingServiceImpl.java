package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.mapper.ThingTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ThingServiceImpl implements ThingService {

    private final ThingEntityRepository thingEntityRepository;

    private final ThingMapper thingMapper;

    private final ThingTypeMapper thingTypeMapper;

    @Override
    public List<Thing> findAll() {
        List<ThingEntity> all = thingEntityRepository.findAll();
        List<Thing> things = thingMapper.thingEntitiesToThings(all);
        return new ArrayList<>(things);
    }

    @Override
    public Thing getById(long id) {
        return thingMapper.thingEntityToThing(thingEntityRepository.getOne(id));
    }

    @Override
    public List<Thing> getAllThingsByType(ThingType type) {
        return thingMapper.thingEntitiesToThings(thingEntityRepository.findAllByType(thingTypeMapper.thingTypeToThingTypeEntity(type)));
    }

    @Override
    public List<Thing> getAllThingPersonGet(Client client) {
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
