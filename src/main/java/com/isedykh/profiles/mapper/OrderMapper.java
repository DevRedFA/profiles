package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.service.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    Order OrderEntityToOrder(OrderEntity OrderEntity);

    OrderEntity OrderToOrderEntity(Order Order);

    List<Order> OrderEntitiesToOrders(List<OrderEntity> OrderEntities);

    List<OrderEntity> OrdersToOrderEntities(List<Order> Order);

}
