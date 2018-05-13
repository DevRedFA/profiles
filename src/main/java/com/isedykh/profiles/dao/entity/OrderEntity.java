package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@ToString(exclude = "client")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders", schema = "public")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 1024)
    private String comments;

    @Column(nullable = false)
    private Timestamp begin;

    @Column(nullable = false)
    private Timestamp stop;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name = "thing_id",
            referencedColumnName = "id",
            nullable = false)
    private ThingEntity thing;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name = "client_id",
            referencedColumnName = "id",
            nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name = "price_id",
            referencedColumnName = "id",
            nullable = false)
    private PriceEntity price;
}
