package com.isedykh.profiles.service.impl;

import com.isedykh.profiles.dao.entity.OrderStatusEntity;
import com.isedykh.profiles.dao.repository.OrderStatusEntityRepository;
import com.isedykh.profiles.mapper.OrderMapper;
import com.isedykh.profiles.service.OrderStatusService;
import com.isedykh.profiles.service.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusEntityRepository orderStatusEntityRepository;

    private final OrderMapper orderMapper;

    @Override
    public List<OrderStatus> findAll() {
        return orderMapper.orderStatusEntitiesToOrderStatuses(orderStatusEntityRepository.findAll());
    }

    @Override
    public Page<OrderStatus> findAll(Pageable pageable) {
        Page<OrderStatusEntity> all = orderStatusEntityRepository.findAll(pageable);
        List<OrderStatus> orderStatuses = orderMapper.orderStatusEntitiesToOrderStatuses(all.getContent());
        return new PageImpl<>(orderStatuses, all.getPageable(), all.getTotalElements());
    }

    @Override
    public OrderStatus save(OrderStatus orderStatus) {
        OrderStatusEntity save = orderStatusEntityRepository.save(orderMapper.orderStatusToOrderStatusEntity(orderStatus));
        return orderMapper.orderStatusEntityToOrderStatus(save);
    }

    @Override
    public void delete(OrderStatus orderStatus) {
        orderStatusEntityRepository.delete(orderMapper.orderStatusToOrderStatusEntity(orderStatus));
    }

    @Override
    public void delete(long id) {
        orderStatusEntityRepository.deleteById(id);
    }

    @Override
    public OrderStatus findById(long id) {
        return orderMapper.orderStatusEntityToOrderStatus(orderStatusEntityRepository.getOne(id));
    }
}
