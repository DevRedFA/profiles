package com.isedykh.profiles.service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of="name")
public class Term {

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