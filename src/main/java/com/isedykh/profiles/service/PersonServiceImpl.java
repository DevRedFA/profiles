package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.PersonEntity;
import com.isedykh.profiles.dao.repository.PersonEntityRepository;
import com.isedykh.profiles.mapper.PersonMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private PersonEntityRepository personEntityRepository;

    private PersonMapper personMapper;

    @Override
    public Person savePerson(Person person) {
        PersonEntity save = personEntityRepository.save(personMapper.personToPersonEntity(person));
        return personMapper.personEntityToPerson(save);
    }

    @Override
    public Person updatePerson(Person person) {
        PersonEntity save = personEntityRepository.save(personMapper.personToPersonEntity(person));
        return personMapper.personEntityToPerson(save);
    }

    @Override
    public void deletePerson(Person person) {
         personEntityRepository.delete(personMapper.personToPersonEntity(person));
    }

    @Override
    public List<Person> findPersonByName(String name) {
        List<PersonEntity> allByName = personEntityRepository.findAllByName(name);
        return personMapper.personEntitiesToPersons(allByName);
    }

    @Override
    public List<Person> findPersonByPhone(long phone) {
        List<PersonEntity> allByPhone = personEntityRepository.findAllByPhoneOrPhoneSecond(phone, phone);
        return personMapper.personEntitiesToPersons(allByPhone);
    }
}
