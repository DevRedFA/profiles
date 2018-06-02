package com.isedykh.profiles.service.impl;

import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.dao.repository.ThingTypeEntityRepository;
import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.ThingTypeService;
import com.isedykh.profiles.service.entity.ThingType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThingTypeServiceImpl implements ThingTypeService {

    private final ThingTypeEntityRepository thingTypeEntityRepository;

    private final ThingMapper thingMapper;

    @Override
    public List<ThingType> findAll() {
        return thingMapper.thingTypeEntitiesToThingTypes(thingTypeEntityRepository.findAll());
    }

    @Override
    public Page<ThingType> findAll(Pageable pageable) {
        Page<ThingTypeEntity> all = thingTypeEntityRepository.findAll(pageable);
        List<ThingType> thingTypes = thingMapper.thingTypeEntitiesToThingTypes(all.getContent());
        return new PageImpl<>(thingTypes, all.getPageable(), all.getTotalElements());
    }

    @Override
    public ThingType save(ThingType thingType) {
        ThingTypeEntity save = thingTypeEntityRepository.save(thingMapper.thingTypeToThingTypeEntity(thingType));
        return thingMapper.thingTypeEntityToThingType(save);
    }

    @Override
    public void delete(ThingType thingType) {
        thingTypeEntityRepository.delete(thingMapper.thingTypeToThingTypeEntity(thingType));
    }

    @Override
    public void delete(long id) {
        thingTypeEntityRepository.deleteById(id);
    }

    @Override
    public ThingType findById(long id) {
        return thingMapper.thingTypeEntityToThingType(thingTypeEntityRepository.getOne(id));
    }
}
