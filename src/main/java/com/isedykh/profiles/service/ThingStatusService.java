package com.isedykh.profiles.service;

import com.isedykh.profiles.service.CrudService;
import com.isedykh.profiles.service.entity.ThingStatus;
import com.isedykh.profiles.service.entity.ThingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ThingStatusService extends CrudService<ThingStatus> {

    List<ThingStatus> findAll();

    Page<ThingStatus> findAll(Pageable pageable);

    ThingStatus save(ThingStatus thingStatus);

    void delete(ThingStatus thingStatus);

    ThingStatus findById(long id);
}
