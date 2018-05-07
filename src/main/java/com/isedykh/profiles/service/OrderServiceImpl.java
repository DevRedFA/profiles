package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.repository.OrderEntityRepository;
import com.isedykh.profiles.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderEntityRepository orderEntityRepository;

    private final OrderMapper orderMapper;

    @Override
    public Order findById(long id) {
        return orderMapper.orderEntityToOrder(orderEntityRepository.getOne(id));
    }

    @Override
    public List<Order> getClientOrderHistory(Client client) {
        return orderMapper.orderEntitiesToOrders(orderEntityRepository.findAllByClientId(client.getId()));
    }

    @Override
    public List<Order> getClientOrderHistory(long clientId) {
        return orderMapper.orderEntitiesToOrders(orderEntityRepository.findAllByClientId(clientId));
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        Page<OrderEntity> all = orderEntityRepository.findAll(pageable);
        List<Order> orders = orderMapper.orderEntitiesToOrders(all.getContent());
        return new PageImpl<>(orders, all.getPageable(), all.getTotalElements());
    }

    @Override
    public void delete(Order order) {

    }

    @Override
    public Order updateOrder(Order order) {
        OrderEntity orderEntity = orderMapper.orderToOrderEntity(order);
        return orderMapper.orderEntityToOrder(orderEntityRepository.save(orderEntity));
    }

    @Override
    public void deleteOrder(Order order) {
        orderEntityRepository.delete(orderMapper.orderToOrderEntity(order));
    }

    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = orderMapper.orderToOrderEntity(order);
        return orderMapper.orderEntityToOrder(orderEntityRepository.save(orderEntity));
    }
}
