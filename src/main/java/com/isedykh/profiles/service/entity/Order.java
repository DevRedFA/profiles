package com.isedykh.profiles.service.entity;

import com.isedykh.profiles.dao.entity.OrderStatus;
import com.isedykh.profiles.service.Identifiable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Order implements Identifiable {

    private Long id;

    private LocalDate begin;

    private LocalDate stop;

    private OrderStatus status;

    private Thing thing;

    private Client client;

    private Price price;

    private String comments;
}