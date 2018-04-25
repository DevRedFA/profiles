package com.isedykh.profiles.Service;

import java.util.List;

public interface PersonService {

    Person savePerson(Person person);

    Person updatePerson(Person person);

    boolean deletePerson(Person person);


    // TODO: 4/12/18 Think about full text search instead 
    List<Person> findPersonByName(String string);

    List<Person> findPersonByPhone(long string);
}
