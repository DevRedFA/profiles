package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.PersonEntity;
import com.isedykh.profiles.service.Person;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {

    Person personEntityToPerson(PersonEntity personEntity);

    PersonEntity personToPersonEntity(Person person);

    List<Person> personEntitiesToPersons(List<PersonEntity> personEntities);

    List<PersonEntity> personsToPersonEntities(List<Person> person);

}
