package com.isedykh.profiles.service;

import lombok.Data;

import java.util.Date;

@Data
public class Order implements Identifiable {

    private long id;

    private Date begin;

    private Date end;

    private OrderStatus status;

    private Thing thing;

    private Client client;

    private int price;

    private String comments;
}
