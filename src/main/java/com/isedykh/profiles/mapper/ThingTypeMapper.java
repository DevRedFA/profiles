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

    ThingType ThingTypeEntityToThingType(ThingTypeEntity ThingTypeEntity);

    ThingTypeEntity ThingTypeToThingTypeEntity(ThingType ThingType);

    List<ThingType> ThingTypeEntitiesToThingTypes(List<ThingTypeEntity> ThingTypeEntities);

    List<ThingTypeEntity> ThingTypesToThingTypeEntities(List<ThingType> ThingType);

}
