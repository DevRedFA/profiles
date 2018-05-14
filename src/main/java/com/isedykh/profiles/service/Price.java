package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.Term;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = "thing")
public class Price {

    private Long id;

    private Term term;

    //1 penny step
    private Integer priceValue;

    private Thing thing;

    @Override
    public String toString() {
        return term + ", " + priceValue;
    }
}
