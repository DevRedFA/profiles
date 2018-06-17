package com.isedykh.profiles.dao.entity;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Data
@Table(name = "users", schema = "public")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public UserEntity() {
    }

    public UserEntity(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    @CreatedDate
    @Type(type = "java.sql.Timestamp")
    @Column(updatable = false)
    private Date createdDate;

    @CreatedDate
    @Type(type = "java.sql.Timestamp")
    @Column(updatable = false)
    private Date lastModifiedDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RoleEntity> roles;
}
