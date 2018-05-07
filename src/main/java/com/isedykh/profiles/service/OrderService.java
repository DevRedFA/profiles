package com.isedykh.profiles.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService extends PageableService<Order> {

    Order findById(long id);

    List<Order> getClientOrderHistory(Client client);

    List<Order> getClientOrderHistory(long personId);

    Page<Order> findAll(Pageable pageable);

    Order updateOrder(Order order);

    void deleteOrder(Order order);

    Order createOrder(Order order);
}
