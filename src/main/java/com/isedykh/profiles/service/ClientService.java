package com.isedykh.profiles.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    List<Client>  findAll(Pageable pageable);

    Client savePerson(Client client);

    Client updatePerson(Client client);

    void deletePerson(Client client);

    // TODO: 4/12/18 Think about full text search instead
    List<Client> findPersonByName(String string);

    List<Client> findPersonByPhone(long string);

    Client findById(long id);

}
