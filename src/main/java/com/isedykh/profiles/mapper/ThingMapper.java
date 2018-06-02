package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.service.entity.Thing;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThingMapper {


    Thing thingEntityToThing(ThingEntity thingEntity);

    ThingEntity thingToThingEntity(Thing thing);

    List<Thing> thingEntitiesToThings(List<ThingEntity> thingEntities);

    List<ThingEntity> thingsToThingEntities(List<Thing> thing);}
