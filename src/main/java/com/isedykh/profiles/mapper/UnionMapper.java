package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.service.Order;
import org.modelmapper.ModelMapper;

public class UnionMapper {

    private static final  ModelMapper modelMapper = new ModelMapper();

    public static Order orderEntityToOrder(OrderEntity entity) {
        return modelMapper.map(entity, Order.class);
    }

    public static OrderEntity orderToOrderEntity(Order order) {
        return modelMapper.map(order, OrderEntity.class);
    }
}
