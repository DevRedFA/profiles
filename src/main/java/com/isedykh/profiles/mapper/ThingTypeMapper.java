package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.service.ThingType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThingTypeMapper {

    ThingType thingTypeEntityToThingType(ThingTypeEntity ThingTypeEntity);

    ThingTypeEntity thingTypeToThingTypeEntity(ThingType ThingType);

    List<ThingType> thingTypeEntitiesToThingTypes(List<ThingTypeEntity> ThingTypeEntities);

    List<ThingTypeEntity> thingTypesToThingTypeEntities(List<ThingType> ThingType);

}
