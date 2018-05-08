package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.dao.entity.ThingType;
import com.isedykh.profiles.dao.repository.OrderEntityRepository;
import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import com.isedykh.profiles.mapper.ThingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ThingServiceImpl implements ThingService {

    private final ThingEntityRepository thingEntityRepository;

    private final OrderEntityRepository orderEntityRepository;

    private final ThingMapper thingMapper;

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
        return thingMapper.thingEntitiesToThings(thingEntityRepository.findAllByType(type));
    }

    @Override
    public List<Thing> getAllThingPersonGet(Client client) {
        List<OrderEntity> orderEntities = orderEntityRepository.findAllByClientId(client.getId());
        List<ThingEntity> thingEntities = orderEntities.stream().map(OrderEntity::getThing).distinct().collect(Collectors.toList());
        return thingMapper.thingEntitiesToThings(thingEntities);
    }

    @Override
    public List<Thing> getAllThingPersonGet(long clientId) {
        List<OrderEntity> orderEntities = orderEntityRepository.findAllByClientId(clientId);
        List<ThingEntity> thingEntities = orderEntities.stream().map(OrderEntity::getThing).distinct().collect(Collectors.toList());
        return thingMapper.thingEntitiesToThings(thingEntities);
    }

    @Override
    public Page<Thing> findAll(Pageable pageable) {
        Page<ThingEntity> all = thingEntityRepository.findAll(pageable);
        List<Thing> things = thingMapper.thingEntitiesToThings(all.getContent());
        return new PageImpl<>(things, all.getPageable(), all.getTotalElements());
    }

    @Override
    public void delete(Thing thing) {
        ThingEntity thingEntity = thingMapper.thingToThingEntity(thing);
        thingEntityRepository.delete(thingEntity);
    }

    @Override
    public Page<ThingDto> findAllToDto(Pageable pageable) {
        Page<ThingEntity> all = thingEntityRepository.findAll(pageable);
        List<Thing> things = thingMapper.thingEntitiesToThings(all.getContent());
        List<ThingDto> thingDtos = things.stream().map(thingMapper::thingToThingDto).collect(Collectors.toList());
        return new PageImpl<>(thingDtos, all.getPageable(), all.getTotalElements());
    }

    @Override
    public Thing save(Thing thing) {
        ThingEntity thingEntity = thingMapper.thingToThingEntity(thing);
        ThingEntity save = thingEntityRepository.save(thingEntity);
        return thingMapper.thingEntityToThing(save);
    }

    @Override
    public Thing update(Thing thing) {
        ThingEntity thingEntity = thingMapper.thingToThingEntity(thing);
        ThingEntity save = thingEntityRepository.save(thingEntity);
        return thingMapper.thingEntityToThing(save);
    }
}
