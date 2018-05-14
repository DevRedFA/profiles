package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Data
@Entity
@ToString(exclude = "thing")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prices", schema = "public")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Term term;

    //1 penny step
    @Column(nullable = false)
    private Integer priceValue;

    @ManyToOne(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "thing_id",
            referencedColumnName = "id",
            nullable = false)
    private ThingEntity thing;
}
