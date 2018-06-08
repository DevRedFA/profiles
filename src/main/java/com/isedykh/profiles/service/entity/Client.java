package com.isedykh.profiles.service.entity;


import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = "name")
public class Client implements Identifiable, Nameable {

    private Long id;

    private String name;

    private Long phone;

    private Long phoneSecond;

    private String address;

    private Integer childrenNumber;

    private String childrenComments;

    private String email;

    private String vkLink;
}
