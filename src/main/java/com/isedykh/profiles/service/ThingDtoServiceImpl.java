package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ThingType;
import com.isedykh.profiles.mapper.ThingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThingDtoServiceImpl implements ThingDtoService {

    private final ThingService thingService;

    private final ThingMapper thingMapper;

    @Override
    public Page<ThingDto> findAll(Pageable pageable) {
        return thingService.findAllToDto(pageable);
    }

    @Override
    public List<ThingDto> getAllThingsByTypeFreeBetween(ThingType type, LocalDate begin, LocalDate end) {
        return thingMapper.ThingsToThingDtos(thingService.getAllThingsByTypeFreeBetween(type, begin, end));
    }

    @Override
    public void delete(ThingDto thingDto) {

    }
}
