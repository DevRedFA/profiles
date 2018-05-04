package com.isedykh.profiles.service;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Thing {

    private String name;

    private int purchasePrice;

    private LocalDate purchaseDate;

    private byte[] photo;

    private String pathToPhoto;

    private ThingType type;

    private ThingStatus status;

    private List<Price> prices;

    private int deposit;
}
