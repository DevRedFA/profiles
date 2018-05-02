package com.isedykh.profiles.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
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
    private Timestamp end;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_type_id",
            referencedColumnName = "id",
            nullable = false)
    private OrderTypeEntity orderType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id",
            referencedColumnName = "id",
            nullable = false)
    private StatusEntity status;

//    @NonNull
//    @Column(name = "thing", nullable = false)
//    private ThingEntity thing;
//
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id",
            referencedColumnName = "id",
            nullable = false)
    private PersonEntity person;

    @Column(nullable = false)
    private int price;



}
