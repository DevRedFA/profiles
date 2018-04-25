package com.isedykh.profiles.Dao.Entity;

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

    @Column(nullable = false)
    private OrderTypeEntity orderType;
//
//    @NonNull
//    @Column(name = "order_type", nullable = false)
//    private StatusEntity status;
//
//    @NonNull
//    @Column(name = "thing", nullable = false)
//    private ThingEntity thing;
//
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id",
            referencedColumnName = "id",
            nullable = false)
    private PersonEntity person;
//
//    @NonNull
//    @Column(name = "price", nullable = false)
//    private int price;
//


}
