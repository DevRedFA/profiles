package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ClientServiceImpl.class})
public class ClientServiceImplTest {

    private ClientService clientService;

    @MockBean
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
