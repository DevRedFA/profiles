package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.service.entity.ThingType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThingTypeMapper {

    ThingType thingEntityTypeToThingType(ThingTypeEntity thingTypeEntity);

    ThingTypeEntity thingTypeToThingTypeEntity(ThingType thingType);

    List<ThingType> thingEntitiesTypeToThingTypes(List<ThingTypeEntity> thingTypeEntity);

    List<ThingTypeEntity> thingTypesToThingTypeEntities(List<ThingType> thingType);
}
