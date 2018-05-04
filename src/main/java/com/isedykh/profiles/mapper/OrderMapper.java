package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.service.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "client", ignore = true)
    Order orderEntityToOrder(OrderEntity orderEntity);

    OrderEntity orderToOrderEntity(Order order);

    List<Order> orderEntitiesToOrders(List<OrderEntity> orderEntities);

    List<OrderEntity> ordersToOrderEntities(List<Order> order);

}
