package com.isedykh.profiles.service;

import com.isedykh.profiles.service.entity.Client;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.entity.OrderStatus;
import com.isedykh.profiles.service.entity.Thing;
import com.isedykh.profiles.service.entity.ThingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService extends CrudService<Order> {

    Order findById(long id);

    List<Order> getThingOrderHistory(Thing thing);

    List<Order> getThingOrderHistory(long thingId);

    List<Order> getClientOrderHistory(Client client);

    List<Order> getAllOrdersWithThingType(ThingType thingType);

    Page<Order> findAll(Pageable pageable);

    Order update(Order order);

    void delete(Order order);

    Order save(Order order);

    List<OrderStatus> getAllOrderStatuses();

    void delete(List<Order> list);

    void deleteByClient(Client client);

    void closeOrder(Long id);
}
