package com.isedykh.profiles.service;

import com.isedykh.profiles.service.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderStatusService extends CrudService<OrderStatus> {

    List<OrderStatus> findAll();

    Page<OrderStatus> findAll(Pageable pageable);

    OrderStatus save(OrderStatus OrderStatus);

    void delete(OrderStatus OrderStatus);

    OrderStatus findById(long id);
}
