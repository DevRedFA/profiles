package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService extends CrudService<Client> {

    List<Client> findAll();

    Page<Client> findAll(Pageable pageable);

    Client save(Client client);

    void delete(Client client);

    List<Client> findClientByName(String string);

    List<Client> findClientByPhone(long string);

    Client findById(long id);

}
