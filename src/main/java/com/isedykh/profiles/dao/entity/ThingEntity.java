package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private ThingType type;

    @Enumerated(EnumType.STRING)
    private ThingStatus status;

    @OneToMany(mappedBy = "thing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PriceEntity> prices;

    @Column(nullable = false)
    private int deposit;

//    @OneToMany(mappedBy = "thing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<OrderEntity> orders;

    private String comments;
}
