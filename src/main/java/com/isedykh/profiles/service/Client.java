package com.isedykh.profiles.service;


import lombok.Data;

import java.util.List;

@Data
public class Client implements Identifiable {

    private long id;

    private String name;

    private long phone;

    private long phoneSecond;

    private String address;

    private int childrenNumber;

    private String childrenComments;

    private String email;

    private String contactLink;

    private List<Order> orders;
}
