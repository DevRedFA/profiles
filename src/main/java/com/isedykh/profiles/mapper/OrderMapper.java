package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {


    Order orderEntityToOrder(OrderEntity orderEntity);

    @Mapping(target = "orders", ignore = true)
    Client clientEntityToClient(ClientEntity clientEntity);

    OrderEntity orderToOrderEntity(Order order);

    List<Order> orderEntitiesToOrders(List<OrderEntity> orderEntities);

    List<OrderEntity> ordersToOrderEntities(List<Order> order);

    default java.sql.Timestamp map(java.time.LocalDate value) {
        return Timestamp.valueOf(value.atStartOfDay());
    }

}
