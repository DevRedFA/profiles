package com.isedykh.profiles.Service;

import lombok.Data;

import java.time.LocalDate;
import java.util.EnumMap;

public class Thing {

    private String name;
    private int purchasePrice;
    private LocalDate purchaseDate;
    private byte[] photo;
    private ThingType type;
    private int deposit;
    private EnumMap<Term, Integer> prices;


}
