package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "things", schema = "public")
public class ThingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int purchasePrice;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Transient
    private byte[] photo;

    @Column(length = 1024)
    private String pathToPhoto;

    @Enumerated(EnumType.STRING)
    private ThingType type;

    @Enumerated(EnumType.STRING)
    private ThingStatus status;

    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.REFRESH},  fetch = FetchType.EAGER)
    @JoinColumn(name = "thing_id")
    private List<PriceEntity> prices;

    @Column(nullable = false)
    private int deposit;

    @Column(length = 1024)
    private String comments;
}
