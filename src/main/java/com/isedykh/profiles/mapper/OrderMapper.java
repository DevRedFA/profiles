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
import java.util.ArrayList;
import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {


    default Order orderEntityToOrder(OrderEntity orderEntity) {
        Client client = clientEntityToClient(orderEntity.getClient());
        List<Order> orders = this.orderEntitiesToOrders(orderEntity.getClient().getOrders(), new Object());
        client.setOrders(new ArrayList<>(orders));
        orders.forEach(s -> s.setClient(client));
        orders.removeIf(s -> s.getId() != orderEntity.getId());
        return orders.get(0);
    }

    @Mapping(target = "client", ignore = true)
    Order orderEntityToOrder(OrderEntity orderEntity, Object obj);


    @Mapping(target = "orders", ignore = true)
    Client clientEntityToClient(ClientEntity clientEntity);

    OrderEntity orderToOrderEntity(Order order);

    List<Order> orderEntitiesToOrders(List<OrderEntity> orderEntities);

    default List<Order> orderEntitiesToOrders(List<OrderEntity> orderEntities, Object object) {
        ArrayList<Order> orders = new ArrayList<>();
        orderEntities.forEach(s -> orders.add(this.orderEntityToOrder(s, object)));
        return orders;
    }

    ;

    List<OrderEntity> ordersToOrderEntities(List<Order> order);

    default java.sql.Timestamp map(java.time.LocalDate value) {
        return Timestamp.valueOf(value.atStartOfDay());
    }

}
