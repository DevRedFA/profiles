package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService extends CrudService<Client> {

    List<Client> findAll();

    Page<Client> findAll(Pageable pageable);

    Client save(Client client);

    Client update(Client client);

    void delete(Client client);

    // TODO: 4/12/18 Think about full text search instead
    List<Client> findClientByName(String string);

    List<Client> findClientByPhone(long string);

    Client findByOrder(Order order);

    List<Client> findByOrders(List<Order> orders);

    Client findById(long id);

}
