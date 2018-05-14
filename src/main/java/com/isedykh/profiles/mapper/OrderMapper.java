package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.entity.PriceEntity;
import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.Order;
import com.isedykh.profiles.service.Price;
import com.isedykh.profiles.service.Thing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        orders.get(0).getPrice().setThing(orders.get(0).getThing());
        return orders.get(0);
    }

    @Mapping(target = "client", ignore = true)
    Order orderEntityToOrder(OrderEntity orderEntity, Object obj);

    default Thing thingEntityToThing(ThingEntity thingEntity) {
        Thing thing = thingEntityToThing(thingEntity, new Object());
        List<Price> prices = priceEntitiesToPrices(thingEntity.getPrices());
        prices.forEach(s -> s.setThing(thing));
        thing.setPrices(prices);
        return thing;
    }

    @Mapping(target = "prices", ignore = true)
    Thing thingEntityToThing(ThingEntity thingEntity, Object object);

    @Mapping(target = "thing", ignore = true)
    Price priceEntityToPrice(PriceEntity priceEntity);

    List<Price> priceEntitiesToPrices(List<PriceEntity> priceEntity);

    @Mapping(target = "orders", ignore = true)
    Client clientEntityToClient(ClientEntity clientEntity);

    @Mapping(target = "orders", ignore = true)
    ClientEntity clientToClientEntity(Client client);

    @Mapping(target = "thing", ignore = true)
    PriceEntity priceToPriceEntity(Price price);

    default OrderEntity orderToOrderEntity(Order order) {
        ClientEntity clientEntity = clientToClientEntity(order.getClient());
        List<OrderEntity> orders = this.ordersToOrderEntities(order.getClient().getOrders(), new Object());
        clientEntity.setOrders(new ArrayList<>(orders));
        OrderEntity orderEntity = orderToOrderEntity(order, new Object());
        orderEntity.setClient(clientEntity);
        clientEntity.getOrders().forEach(s -> s.setClient(clientEntity));
        if (!clientEntity.getOrders().contains(orderEntity)) {
            clientEntity.getOrders().add(orderEntity);
        }
        orderEntity.getPrice().setThing(orderEntity.getThing());
        if (orderEntity.getThing().getOrders() != null) {
            orderEntity.getThing().getOrders().add(orderEntity);
        } else {
            orderEntity.getThing().setOrders(Collections.singletonList(orderEntity));
        }
        orderEntity.getThing().getPrices().forEach(s -> s.setThing(orderEntity.getThing()));
        return orderEntity;
    }


    @Mapping(target = "client", ignore = true)
    OrderEntity orderToOrderEntity(Order order, Object object);

    List<Order> orderEntitiesToOrders(List<OrderEntity> orderEntities);

    default List<Order> orderEntitiesToOrders(List<OrderEntity> orderEntities, Object object) {
        ArrayList<Order> orders = new ArrayList<>();
        orderEntities.forEach(s -> orders.add(this.orderEntityToOrder(s, object)));
        return orders;
    }

    default List<OrderEntity> ordersToOrderEntities(List<Order> orders, Object object) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orders.forEach(s -> orderEntities.add(this.orderToOrderEntity(s, object)));
        return orderEntities;
    }

    List<OrderEntity> ordersToOrderEntities(List<Order> order);

    default java.sql.Timestamp map(java.time.LocalDate value) {
        return Timestamp.valueOf(value.atStartOfDay());
    }
}
