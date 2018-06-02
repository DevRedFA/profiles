package com.isedykh.profiles.service;

import com.isedykh.profiles.service.entity.Client;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.entity.Thing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService extends CrudService<Order> {

    Order findById(long id);

    List<Order> getThingOrderHistory(Thing thing);

    List<Order> getClientOrderHistory(Client client);

    Page<Order> findAll(Pageable pageable);

    Order update(Order order);

    void delete(Order order);

    Order save(Order order);
}
