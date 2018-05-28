package com.isedykh.profiles.service;

import lombok.Data;

@Data
public class ThingType {

    private Long id;

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
