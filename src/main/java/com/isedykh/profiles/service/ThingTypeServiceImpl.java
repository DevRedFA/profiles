package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.dao.repository.ThingTypeEntityRepository;
import com.isedykh.profiles.mapper.ThingTypeMapper;
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

    private final ThingTypeMapper thingTypeMapper;

    @Override
    public List<ThingType> findAll() {
        return thingTypeMapper.thingEntitiesTypeToThingTypes(thingTypeEntityRepository.findAll());
    }

    @Override
    public Page<ThingType> findAll(Pageable pageable) {
        Page<ThingTypeEntity> all = thingTypeEntityRepository.findAll(pageable);
        List<ThingType> thingTypes = thingTypeMapper.thingEntitiesTypeToThingTypes(all.getContent());
        return new PageImpl<>(thingTypes, all.getPageable(), all.getTotalElements());
    }

    @Override
    public ThingType save(ThingType thingType) {
        ThingTypeEntity save = thingTypeEntityRepository.save(thingTypeMapper.thingTypeToThingTypeEntity(thingType));
        return thingTypeMapper.thingEntityTypeToThingType(save);
    }

    @Override
    public void delete(ThingType thingType) {
        thingTypeEntityRepository.delete(thingTypeMapper.thingTypeToThingTypeEntity(thingType));
    }

    @Override
    public void delete(long id) {
        thingTypeEntityRepository.deleteById(id);
    }

    @Override
    public ThingType findById(long id) {
        return thingTypeMapper.thingEntityTypeToThingType(thingTypeEntityRepository.getOne(id));
    }
}
