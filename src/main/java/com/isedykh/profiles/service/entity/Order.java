package com.isedykh.profiles.service.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
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
