package com.isedykh.profiles.service.entity;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class Client implements Identifiable, Nameable {

    private Long id;

    private String name;

    private Long phone;

    private Long phoneSecond;

    private List<byte[]> photos;

    private List<String> pathsToPhoto;

    private String address;

    private Integer childrenNumber;

    private String childrenComments;

    private String email;

    private String vkLink;
}
