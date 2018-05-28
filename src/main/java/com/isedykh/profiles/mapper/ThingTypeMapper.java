package com.isedykh.profiles.mapper;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.dao.entity.Term;
import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.service.Price;
import com.isedykh.profiles.service.Thing;
import com.isedykh.profiles.service.ThingDto;
import com.isedykh.profiles.service.ThingType;
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
