package com.isedykh.profiles.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findById(long id) {
        return null;
    }

    @Override
    public List<Order> getPersonOrderHistory(Client client) {
        return null;
    }

    @Override
    public List<Order> getPersonOrderHistory(long personId) {
        return null;
    }

    @Override
    public Order updateOrder(Order order) {
        return null;
    }

    @Override
    public boolean deleteOrder(Order order) {
        return false;
    }

    @Override
    public Order createOrder(Order order) {
        return null;
    }
}
