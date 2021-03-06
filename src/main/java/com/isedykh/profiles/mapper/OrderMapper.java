package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.entity.OrderStatusEntity;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.entity.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    Order orderEntityToOrder(OrderEntity orderEntity);

    OrderEntity orderToOrderEntity(Order order);

    List<Order> orderEntitiesToOrders(List<OrderEntity> orderEntities);

    List<OrderEntity> ordersToOrderEntities(List<Order> order);

    OrderStatus orderStatusEntityToOrderStatus(OrderStatusEntity orderStatusEntity);

    OrderStatusEntity orderStatusToOrderStatusEntity(OrderStatus orderStatus);

    List<OrderStatus> orderStatusEntitiesToOrderStatuses(List<OrderStatusEntity> orderStatusEntity);

    List<OrderStatusEntity> orderStatusesToOrderStatusEntities(List<OrderStatus> orderStatus);

    default java.sql.Timestamp map(java.time.LocalDate value) {
        return Timestamp.valueOf(value.atStartOfDay());
    }
}
