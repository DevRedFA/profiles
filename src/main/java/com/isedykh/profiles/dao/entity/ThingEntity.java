package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "things", schema = "public")
public class ThingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int purchasePrice;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Transient
    private byte[] photo;

    @Column(nullable = false)
    private String pathToPhoto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thing_id",
            referencedColumnName = "id",
            nullable = false)
    private ThingTypeEntity type;

    @Column(nullable = false)
    private int deposit;

}
