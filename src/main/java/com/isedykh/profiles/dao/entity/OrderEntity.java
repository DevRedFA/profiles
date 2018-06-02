package com.isedykh.profiles.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_status_id",
            referencedColumnName = "id",
            nullable = false)
    private OrderStatusEntity status;

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
