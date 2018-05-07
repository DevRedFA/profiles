package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService extends PageableService<Client> {

    List<Client> findAll();

    Page<Client> findAll(Pageable pageable);

    Client saveClient(Client client);

    Client updateClient(Client client);

    void deleteClient(Client client);

    // TODO: 4/12/18 Think about full text search instead
    List<Client> findClientByName(String string);

    List<Client> findClientByPhone(long string);

    Client findById(long id);

}
