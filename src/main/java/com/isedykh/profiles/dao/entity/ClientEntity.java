package com.isedykh.profiles.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


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

    @Transient
    private List<byte[]> photos;

    @ElementCollection
    private List<String> pathsToPhoto;

    private Long phoneSecond;

    private String address;

    private Integer childrenNumber;

    @Column(length = 1024)
    private String childrenComments;

    private String email;

    private String vkLink;
}
