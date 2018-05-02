package com.isedykh.profiles.service;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {

    private LocalDateTime from;
    private LocalDateTime to;
    private OrderType orderType;
    private Status status;
    private Thing thing;
    private Person person;
    private int price;
    private String comments;

}
