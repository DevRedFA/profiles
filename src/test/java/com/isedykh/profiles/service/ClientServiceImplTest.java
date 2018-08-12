package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import com.isedykh.profiles.service.impl.ClientServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = {ClientServiceImpl.class})
public class ClientServiceImplTest {

    private ClientService clientService;

//    @MockBean
//    private ClientEntityRepository clientEntityRepository;

    @Test
    public void savePerson(){
        System.out.println(tableSizeFor(100));
        System.out.println(tableSizeFor(75));
        System.out.println(tableSizeFor(13));
    }

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    };

    @Test
    public void findByPhone(){}

    @Test
    public void findByPhoneSecond(){}

}
