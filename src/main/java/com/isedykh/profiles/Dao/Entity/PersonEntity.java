package com.isedykh.profiles.Dao.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "person", schema = "public")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long phone;

    private long phoneSecond;

    private String address;

    private int childrenNumber;

    @Column(length = 1024)
    private String childrenComments;

    private String email;

    private String contactLink;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderEntity> orders;
}