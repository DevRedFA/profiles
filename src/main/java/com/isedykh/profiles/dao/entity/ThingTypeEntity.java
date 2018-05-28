package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//    ERGO, SLEEP_BAG, CHILD_CARRIER, CHILD_BIKE_SEAT, CHILD_BIKE_WITH_HAND



@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "thing_types", schema = "public")
public class ThingTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
