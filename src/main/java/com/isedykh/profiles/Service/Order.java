package com.isedykh.profiles.Service;

import java.time.LocalDateTime;

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
