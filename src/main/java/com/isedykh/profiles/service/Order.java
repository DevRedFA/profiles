package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Date;

@Data
@EqualsAndHashCode(exclude = "client")
public class Order implements Identifiable {

    private long id;

    private LocalDate begin;

    private LocalDate stop;

    private OrderStatus status;

    private Thing thing;

    private Client client;

    private Price price;

    private String comments;
}
