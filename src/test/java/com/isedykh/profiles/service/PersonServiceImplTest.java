package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.repository.PersonEntityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PersonServiceImpl.class})
public class PersonServiceImplTest {

    private PersonService personService;

    @MockBean
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
