package com.isedykh.profiles.service.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Thing implements Identifiable {

    private Long id;

    private String name;

    private int purchasePrice;

    private LocalDate purchaseDate;

    private byte[] photo;

    private String pathToPhoto;

    private ThingType type;

    private List<Price> prices;

    private int deposit;

    private String comments;
}