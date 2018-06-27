package com.isedykh.profiles.service.impl;

import com.isedykh.profiles.dao.entity.ThingStatusEntity;
import com.isedykh.profiles.dao.repository.ThingStatusEntityRepository;
import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.ThingStatusService;
import com.isedykh.profiles.service.entity.ThingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThingStatusServiceImpl implements ThingStatusService {

    private final ThingStatusEntityRepository thingStatusEntityRepository;

    private final ThingMapper thingMapper;

    @Override
    public List<ThingStatus> findAll() {
        return thingMapper.thingStatusEntitiesToThingStatuses(thingStatusEntityRepository.findAll());
    }

    @Override
    public Page<ThingStatus> findAll(Pageable pageable) {
        Page<ThingStatusEntity> all = thingStatusEntityRepository.findAll(pageable);
        List<ThingStatus> thingStatuses = thingMapper.thingStatusEntitiesToThingStatuses(all.getContent());
        return new PageImpl<>(thingStatuses, all.getPageable(), all.getTotalElements());
    }

    @Override
    public ThingStatus save(ThingStatus thingStatus) {
        ThingStatusEntity save = thingStatusEntityRepository.save(thingMapper.thingStatusToThingStatusEntity(thingStatus));
        return thingMapper.thingStatusEntityToThingStatus(save);
    }

    @Override
    public void delete(ThingStatus thingStatus) {
        thingStatusEntityRepository.delete(thingMapper.thingStatusToThingStatusEntity(thingStatus));
    }

    @Override
    public void delete(long id) {
        thingStatusEntityRepository.deleteById(id);
    }

    @Override
    public ThingStatus findById(long id) {
        return thingMapper.thingStatusEntityToThingStatus(thingStatusEntityRepository.getOne(id));
    }
}
