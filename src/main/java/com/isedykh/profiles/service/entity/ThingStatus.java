package com.isedykh.profiles.service.entity;

import lombok.Data;

@Data
public class ThingStatus implements Identifiable, Nameable {

    private Long id;

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
