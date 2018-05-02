package com.isedykh.profiles.service;

import java.util.List;

public interface OrderService {

    List<Order> getPersonOrderHistory(Person person);

    List<Order> getPersonOrderHistory(long personId);

    Order updateOrder(Order order);

    boolean deleteOrder(Order order);

    Order createOrder(Order order);
}
