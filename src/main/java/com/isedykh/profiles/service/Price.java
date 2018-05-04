package com.isedykh.profiles.service;

import lombok.Data;

@Data
public class Price {

    private Term term;

    //1 penny step
    private Integer price;
}
