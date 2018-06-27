package com.isedykh.profiles.security;

import com.isedykh.profiles.dao.entity.UserEntity;
import com.isedykh.profiles.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        final UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity ==null) {
            throw new UsernameNotFoundException("Username Not Found Exception : " + username);
        }
        return new SecurityUserDetails(userEntity);
    }
}
