package com.isedykh.profiles;

import com.isedykh.profiles.common.InitUtils;
import com.isedykh.profiles.dao.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

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


//        listThing = initUtils.updateThings();

        //init Prices
        initUtils.initPrices(listThing, terms);

        //init Client
        List<ClientEntity> listClient = initUtils.getClientEntities();

        listThing = initUtils.updateThings();


        //init Order
        List<OrderEntity> orderEntities = initUtils.getOrderEntities(listThing, listClient);

        // TODO: 5/6/18 declarative validation in registration form
    }
}
