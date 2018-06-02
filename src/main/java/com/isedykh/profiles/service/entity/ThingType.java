package com.isedykh.profiles.service.entity;

import com.isedykh.profiles.service.Identifiable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = "name")
public class ThingType implements Identifiable {

    private Long id;

    private String name;
}
