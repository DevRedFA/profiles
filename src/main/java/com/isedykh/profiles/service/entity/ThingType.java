package com.isedykh.profiles.service.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = "name")
public class ThingType implements Identifiable, Nameable {

    private Long id;

    private String name;
}
