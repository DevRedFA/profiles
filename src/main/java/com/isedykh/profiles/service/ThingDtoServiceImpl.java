package com.isedykh.profiles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThingDtoServiceImpl implements ThingDtoService {

    private final ThingService thingService;

    @Override
    public Page<ThingDto> findAll(Pageable pageable) {
        return thingService.findAllToDto(pageable);
    }

    @Override
    public void delete(ThingDto thingDto) {

    }
}
