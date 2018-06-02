package com.isedykh.profiles;

import com.isedykh.profiles.dao.entity.*;
import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import com.isedykh.profiles.dao.repository.OrderEntityRepository;
import com.isedykh.profiles.dao.repository.PriceEntityRepository;
import com.isedykh.profiles.dao.repository.TermEntityRepository;
import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import com.isedykh.profiles.dao.repository.ThingTypeEntityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Profile("testData")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final InitUtils initUtils;

    @Override
    public void run(ApplicationArguments args) {

        //init Terms
        List<TermEntity> terms = initUtils.createTerms();

        //init ThingTypes
        List<ThingTypeEntity> thingTypes = initUtils.createThingTypes();

        //init Thing
        List<ThingEntity> listThing = initUtils.getThingEntities(thingTypes);


        listThing = initUtils.updateThings();

        //init Prices
        initUtils.initPrices(listThing, terms);

        //init Client
        List<ClientEntity> listClient = initUtils.getClientEntities();

        listThing = initUtils.updateThings();


        //init Order
        List<OrderEntity> orderEntities = initUtils.getOrderEntities(listThing, listClient);

        // TODO: 5/6/18 images to things profiles
        // TODO: 5/6/18 drag n drop images
        // TODO: 5/6/18 declarative validation in registration form

    }
}
