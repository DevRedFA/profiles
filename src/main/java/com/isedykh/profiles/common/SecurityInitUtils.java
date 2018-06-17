package com.isedykh.profiles.common;

import com.isedykh.profiles.dao.entity.RoleEntity;
import com.isedykh.profiles.dao.entity.UserEntity;
import com.isedykh.profiles.dao.repository.UserRepository;
import com.isedykh.profiles.security.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;

@Service
@Profile({"securityOn", "userData"})
@RequiredArgsConstructor
public class SecurityInitUtils {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser() {
        Arrays.stream(RoleType.values()).forEach(x -> {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("procaton");
            userEntity.setPassword(passwordEncoder.encode("16051991"));

            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setType(x);
            roleEntity.setUser(userEntity);

            userEntity.setRoles(Collections.singletonList(roleEntity));
            userRepository.save(userEntity);
        });
    }
}
