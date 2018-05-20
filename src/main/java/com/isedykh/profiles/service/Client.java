package com.isedykh.profiles.service;


import lombok.Data;

@Data
public class Client implements Identifiable {

    private Long id;

    private String name;

    private Long phone;

    private Long phoneSecond;

    private String address;

    @Override
    public String toString() {
        return name;
    }

    private Integer childrenNumber;

    private String childrenComments;

    private String email;

    private String vkLink;
}
