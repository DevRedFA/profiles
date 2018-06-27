package com.isedykh.profiles.service.impl;

import com.isedykh.profiles.dao.entity.PriceEntity;
import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.dao.repository.OrderEntityRepository;
import com.isedykh.profiles.dao.repository.PriceEntityRepository;
import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import com.isedykh.profiles.dao.repository.ThingStatusEntityRepository;
import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.ThingService;
import com.isedykh.profiles.service.entity.Thing;
import com.isedykh.profiles.service.entity.ThingStatus;
import com.isedykh.profiles.service.entity.ThingType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ThingServiceImpl implements ThingService {

    private final ThingEntityRepository thingEntityRepository;

    private final OrderEntityRepository orderEntityRepository;

    private final PriceEntityRepository priceEntityRepository;

    private final ThingStatusEntityRepository thingStatusEntityRepository;

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
        ThingTypeEntity thingTypeEntity = thingMapper.thingTypeToThingTypeEntity(type);
        return thingMapper.thingEntitiesToThings(thingEntityRepository.findAllByType(thingTypeEntity));
    }

    @Override
    public List<Thing> getAllThingsByTypeFreeBetween(ThingType type, LocalDate begin, LocalDate end) {
        ThingTypeEntity typeEntity = thingMapper.thingTypeToThingTypeEntity(type);
        List<ThingEntity> allByType = thingEntityRepository.findAllByType(typeEntity);
        List<Thing> things = thingMapper.thingEntitiesToThings(allByType);
        // TODO: 11.05.2018 rework from n+1 db requests to 1 request.
        if (begin == null) {
            begin = LocalDate.of(2100, 1, 1);
        }
        if (end == null) {
            end = LocalDate.of(2100, 1, 2);
        }
        Timestamp endTimestamp = Timestamp.valueOf(end.atStartOfDay());
        Timestamp beginTimestamp = Timestamp.valueOf(begin.atStartOfDay());

        return things.stream()
                .filter(thing ->
                        orderEntityRepository.findAllByThingId(
                                thing.getId())
                                .stream()
                                .anyMatch(ord ->
                                        (ord.getBegin().after(endTimestamp)
                                                || ord.getStop().before(beginTimestamp))))
                .collect(Collectors.toList());
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
    public void delete(long id) {
        thingEntityRepository.deleteById(id);
    }


    @Override
    public Thing save(Thing thing) {
        ThingEntity thingEntity = thingMapper.thingToThingEntity(thing);
        List<PriceEntity> priceEntities = priceEntityRepository.saveAll(thingEntity.getPrices());
        thingEntity.setPrices(priceEntities);
        ThingEntity save = thingEntityRepository.save(thingEntity);
        return thingMapper.thingEntityToThing(save);
    }

    @Override
    public List<ThingStatus> getAllThingStatuses() {
        return thingMapper.thingStatusEntitiesToThingStatuses(thingStatusEntityRepository.findAll());
    }

    @Override
    public ThingStatus getStatusByName(String name) {
        return thingMapper.thingStatusEntityToThingStatus(thingStatusEntityRepository.findByName(name));
    }
}
