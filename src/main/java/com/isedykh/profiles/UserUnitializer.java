package com.isedykh.profiles;

import com.isedykh.profiles.common.SecurityInitUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"userData"})
@RequiredArgsConstructor
public class UserUnitializer implements ApplicationRunner {

    private final SecurityInitUtils securityInitUtils;

    @Override
    public void run(ApplicationArguments args) {
        securityInitUtils.createUser();
    }
}
