package com.isedykh.profiles.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@ToString(exclude = {"thing"})
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id",
            referencedColumnName = "id",
            nullable = false)
    private ClientEntity client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "thing_id",
            referencedColumnName = "id",
            nullable = false)
    private ThingEntity thing;

    @ManyToOne(optional = false)
    @JoinColumn(name = "price_id",
            referencedColumnName = "id",
            nullable = false)
    private PriceEntity price;
}
