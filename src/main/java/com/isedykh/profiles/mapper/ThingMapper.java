package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.dao.entity.ThingStatusEntity;
import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.service.entity.Thing;
import com.isedykh.profiles.service.entity.ThingStatus;
import com.isedykh.profiles.service.entity.ThingType;
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

    List<ThingEntity> thingsToThingEntities(List<Thing> thing);

    ThingType thingTypeEntityToThingType(ThingTypeEntity thingTypeEntity);

    ThingTypeEntity thingTypeToThingTypeEntity(ThingType thingType);

    List<ThingType> thingTypeEntitiesToThingTypes(List<ThingTypeEntity> thingTypeEntity);

    List<ThingTypeEntity> thingTypesToThingTypeEntities(List<ThingType> thingType);

    ThingStatus thingStatusEntityToThingStatus(ThingStatusEntity thingTypeEntity);

    ThingStatusEntity thingStatusToThingStatusEntity(ThingStatus thingType);

    List<ThingStatus> thingStatusEntitiesToThingStatuses(List<ThingStatusEntity> thingTypeEntity);

    List<ThingStatusEntity> thinStatusesToThingStatusEntities(List<ThingStatus> thingType);

}
