package com.isedykh.profiles.service;

import java.util.List;

public interface OrderService {

    Order findById(long id);

    List<Order> getPersonOrderHistory(Client client);

    List<Order> getPersonOrderHistory(long personId);

    Order updateOrder(Order order);

    boolean deleteOrder(Order order);

    Order createOrder(Order order);
}
