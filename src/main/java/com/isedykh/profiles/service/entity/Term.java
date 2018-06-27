package com.isedykh.profiles.service.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Term implements Identifiable, Nameable {

    private Long id;

    private String name;

    private Integer coefficient;

    @Override
    public String toString() {
        return name;
    }

    public Term(String name) {
        this.name = name;
    }
}