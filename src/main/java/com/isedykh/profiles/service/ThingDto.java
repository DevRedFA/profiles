package com.isedykh.profiles.service;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ThingDto {

    private String name;

    private Integer purchasePrice;

    private LocalDate purchaseDate;

    private byte[] photo;

    private String pathToPhoto;

    private ThingType type;

    private ThingStatus status;

    private Integer priceForDay;

    private Integer priceForWeek;

    private Integer priceForTwoWeeks;

    private Integer priceForMonth;

    private Integer deposit;
}
