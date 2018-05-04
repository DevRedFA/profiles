package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@ToString(exclude = "person")
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
    private Timestamp end;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id",
            referencedColumnName = "id",
            nullable = false)
    private OrderStatusEntity status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thing_id",
            referencedColumnName = "id",
            nullable = false)
    private ThingEntity thing;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id",
            referencedColumnName = "id",
            nullable = false)
    private PersonEntity person;

    @Column(nullable = false)
    private int price;
}
