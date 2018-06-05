package com.isedykh.profiles.service.entity;

import lombok.Data;
import lombok.ToString;

@Data
public class ThingStatus implements Identifiable, Nameable {

    private Long id;

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
