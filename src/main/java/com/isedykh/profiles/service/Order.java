package com.isedykh.profiles.service;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Order {

    private Date begin;

    private Date end;

    private OrderType orderType;

    private Status status;

    private Thing thing;

    private Person person;

    private int price;

    private String comments;
}
