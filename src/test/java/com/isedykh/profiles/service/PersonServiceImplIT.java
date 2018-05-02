package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.repository.PersonEntityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
//@DataJpaTest
@SpringBootTest
@RequiredArgsConstructor
//@ContextConfiguration(classes = {PersonServiceImpl.class, PersonEntityRepository.class})
public class PersonServiceImplIT {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonEntityRepository personEntityRepository;

    @Test
    public void savePerson(){
        System.out.printf("asf");
    }

    @Test
    public void findByPhone(){}

    @Test
    public void findByPhoneSecond(){}

}
