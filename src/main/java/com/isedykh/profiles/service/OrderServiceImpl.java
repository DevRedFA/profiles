package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.entity.PriceEntity;
import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import com.isedykh.profiles.dao.repository.OrderEntityRepository;
import com.isedykh.profiles.dao.repository.PriceEntityRepository;
import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import com.isedykh.profiles.mapper.OrderMapper;
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

    private final OrderMapper orderMapper;

    @Override
    public Order findById(long id) {
        return orderMapper.orderEntityToOrder(orderEntityRepository.getOne(id));
    }


    @Override
    public List<Order> getThingOrderHistory(Thing thing) {
        return orderMapper.orderEntitiesToOrders(orderEntityRepository.findAllByThingId(thing.getId()));
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
}
