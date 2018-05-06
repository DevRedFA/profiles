package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Order implements Identifiable {

    private long id;

    private LocalDate begin;

    private LocalDate end;

    private OrderStatus status;

    private Thing thing;

    private Client client;

    private int price;

    private String comments;
}
