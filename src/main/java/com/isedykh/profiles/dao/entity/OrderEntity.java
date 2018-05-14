package com.isedykh.profiles.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@ToString(exclude = {"client", "thing"})
@Entity
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders", schema = "public")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String comments;

    @Column(nullable = false)
    private Timestamp begin;

    @Column(nullable = false)
    private Timestamp stop;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "thing_id",
            referencedColumnName = "id",
            nullable = false)
    private ThingEntity thing;

    @ManyToOne(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "price_id",
            referencedColumnName = "id",
            nullable = false)
    private PriceEntity price;
}
