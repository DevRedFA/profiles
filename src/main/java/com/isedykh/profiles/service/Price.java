package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.Term;
import lombok.Data;

@Data
public class Price {

    private Long id;

    private Term term;

    private Integer priceValue;

    @Override
    public String toString() {
        return term + ", " + priceValue / 100 + "," + priceValue % 100;
    }
}
