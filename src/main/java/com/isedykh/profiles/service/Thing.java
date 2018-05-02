package com.isedykh.profiles.service;

import lombok.Data;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;

@Data
public class Thing {

    private String name;

    private int purchasePrice;

    private LocalDate purchaseDate;

    private byte[] photo;

    private String pathToPhoto;

    private ThingType type;

    private int deposit;

    private HashMap<Term, Integer> prices;

}
