package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService extends CrudService<Order> {

    Order findById(long id);

    List<Order> getClientOrderHistory(Client client);

    List<Order> getClientOrderHistory(long personId);

    Page<Order> findAll(Pageable pageable);

    Order update(Order order);

    void delete(Order order);

    Order save(Order order);
}
