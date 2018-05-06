package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
//@DataJpaTest
@SpringBootTest
@RequiredArgsConstructor
//@ContextConfiguration(classes = {ClientServiceImpl.class, PersonEntityRepository.class})
public class ClientServiceImplIT {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientEntityRepository clientEntityRepository;

    @Test
    public void savePerson(){
        System.out.printf("asf");
    }

    @Test
    public void findByPhone(){}

    @Test
    public void findByPhoneSecond(){}

}
