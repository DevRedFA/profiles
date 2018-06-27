package com.isedykh.profiles.dao.entity;

import com.isedykh.profiles.security.type.RoleType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "roles", schema = "public")
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public RoleEntity() {
        // TODO: 27.06.2018 check if needed
    }

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name="RoleType", nullable = false, unique = true)
    private RoleType type;

    @CreatedDate
    @Type(type="java.sql.Timestamp")
    @Column(updatable = false)
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private UserEntity user;
}
