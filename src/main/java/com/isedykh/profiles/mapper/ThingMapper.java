package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.service.Thing;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThingMapper {

    Thing ThingEntityToThing(ThingEntity ThingEntity);

    ThingEntity ThingToThingEntity(Thing Thing);

    List<Thing> ThingEntitiesToThings(List<ThingEntity> ThingEntities);

    List<ThingEntity> ThingsToThingEntities(List<Thing> Thing);

}
