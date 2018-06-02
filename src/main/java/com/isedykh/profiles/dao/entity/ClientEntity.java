package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients", schema = "public")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long phone;

    private Long phoneSecond;

    private String address;

    private Integer childrenNumber;

    @Column(length = 1024)
    private String childrenComments;

    private String email;

    private String vkLink;
}
