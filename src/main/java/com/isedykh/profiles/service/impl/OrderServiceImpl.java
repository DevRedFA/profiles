package com.isedykh.profiles.service.impl;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import com.isedykh.profiles.dao.repository.OrderEntityRepository;
import com.isedykh.profiles.dao.repository.OrderStatusEntityRepository;
import com.isedykh.profiles.dao.repository.PriceEntityRepository;
import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import com.isedykh.profiles.mapper.OrderMapper;
import com.isedykh.profiles.service.OrderService;
import com.isedykh.profiles.service.entity.Client;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.entity.OrderStatus;
import com.isedykh.profiles.service.entity.Thing;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderEntityRepository orderEntityRepository;

    private final PriceEntityRepository priceEntityRepository;

    private final ThingEntityRepository thingEntityRepository;

    private final ClientEntityRepository clientEntityRepository;

    private final OrderStatusEntityRepository orderStatusEntityRepository;

    private final OrderMapper orderMapper;

    @Override
    public Order findById(long id) {
        return orderMapper.orderEntityToOrder(orderEntityRepository.getOne(id));
    }


    @Override
    public List<Order> getThingOrderHistory(Thing thing) {
        List<OrderEntity> allByThingId = orderEntityRepository.findAllByThingId(thing.getId());
        return orderMapper.orderEntitiesToOrders(allByThingId);
    }

    @Override
    public List<Order> getThingOrderHistory(long thingId) {
        List<OrderEntity> allByThingId = orderEntityRepository.findAllByThingId(thingId);
        return orderMapper.orderEntitiesToOrders(allByThingId);
    }

    @Override
    public List<Order> getClientOrderHistory(Client client) {
        return orderMapper.orderEntitiesToOrders(orderEntityRepository.findAllByClientId(client.getId()));
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        Page<OrderEntity> all = orderEntityRepository.findAll(pageable);
        List<Order> orders = orderMapper.orderEntitiesToOrders(all.getContent());
        return new PageImpl<>(orders, all.getPageable(), all.getTotalElements());
    }

    @Override
    public void delete(Order order) {
        orderEntityRepository.delete(orderMapper.orderToOrderEntity(order));
    }

    @Override
    public void delete(long id) {
        orderEntityRepository.deleteById(id);
    }

    @Override
    public void delete(List<Order> list) {
        List<OrderEntity> orderEntities = orderMapper.ordersToOrderEntities(list);
        orderEntityRepository.deleteAll(orderEntities);
    }

    @Override
    public Order update(Order order) {
        OrderEntity orderEntity = orderMapper.orderToOrderEntity(order);
        return orderMapper.orderEntityToOrder(orderEntityRepository.save(orderEntity));
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderMapper.orderToOrderEntity(order);
        orderEntity.setPrice(priceEntityRepository.save(orderEntity.getPrice()));
        orderEntity.setThing(thingEntityRepository.save(orderEntity.getThing()));
        orderEntity.setClient(clientEntityRepository.save(orderEntity.getClient()));
        return orderMapper.orderEntityToOrder(orderEntityRepository.save(orderEntity));
    }

    @Override
    public List<OrderStatus> getAllOrderStatuses() {
        return orderMapper.orderStatusEntitiesToOrderStatuses(orderStatusEntityRepository.findAll());
    }
}
