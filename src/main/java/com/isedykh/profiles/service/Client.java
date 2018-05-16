package com.isedykh.profiles.service;


import lombok.Data;

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
}
