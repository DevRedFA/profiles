package com.isedykh.profiles.security;

import com.isedykh.profiles.dao.entity.RoleEntity;
import com.isedykh.profiles.dao.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class SecurityUserDetails extends UserEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    public SecurityUserDetails(UserEntity userEntity) {
        if(userEntity ==null) return;
        this.setId(userEntity.getId());
        this.setUsername(userEntity.getUsername());
        this.setPassword(userEntity.getPassword());
        this.setCreatedDate(userEntity.getCreatedDate());
        this.setLastModifiedDate(userEntity.getLastModifiedDate());
        this.setRoles(userEntity.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(RoleEntity roleEntity : super.getRoles()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleEntity.getType().name());
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
