package com.isedykh.profiles.service;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class Client implements Identifiable {

    private Long id;

    private String name;

    private long phone;

    private long phoneSecond;

    private String address;

    @Override
    public String toString() {
        return name;
    }

    private int childrenNumber;

    private String childrenComments;

    private String email;

    private String contactLink;

    private List<Order> orders;
}
