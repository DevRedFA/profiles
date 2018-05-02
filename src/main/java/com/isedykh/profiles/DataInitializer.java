package com.isedykh.profiles;

import com.isedykh.profiles.dao.entity.*;
import com.isedykh.profiles.dao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private ThingTypeEntityRepository thingTypeEntityRepository;

    @Autowired
    private ThingEntityRepository thingEntityRepository;

    @Autowired
    private TermEntityRepository termEntityRepository;

    @Autowired
    private PriceEntityRepository priceEntityRepository;

    @Autowired
    private StatusEntityRepository statusEntityRepository;

    @Autowired
    private OrderTypeEntityRepository orderTypeEntityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //init ThingTypes
        ThingTypeEntity ergo = thingTypeEntityRepository.save(new ThingTypeEntity(1, "ERGO"));
        thingTypeEntityRepository.save(new ThingTypeEntity(2, "SLEEPBAG"));
        thingTypeEntityRepository.save(new ThingTypeEntity(3, "CHILD_CARRIER"));

        //init Things

        TermEntity day = termEntityRepository.save(new TermEntity(1, "DAY"));
        TermEntity week = termEntityRepository.save(new TermEntity(2, "WEEK"));
        TermEntity twoWeeks = termEntityRepository.save(new TermEntity(3, "TWO_WEEKS"));
        TermEntity month = termEntityRepository.save(new TermEntity(4, "MONTH"));

        //init Thing
        ThingEntity thing = new ThingEntity(1, "Thing 1",
                1000, LocalDate.now(), null, "c:/pathToPhoto", ergo, 300);
        thing = thingEntityRepository.save(thing);

        //init Prices
        PriceEntity priceDay = new PriceEntity(1, day, 100, thing);
        PriceEntity priceWeek = new PriceEntity(2, week, 300, thing);
        PriceEntity priceTwoWeeks = new PriceEntity(3, twoWeeks, 500, thing);

        priceEntityRepository.save(priceDay);
        priceEntityRepository.save(priceWeek);
        priceEntityRepository.save(priceTwoWeeks);

        //init Status
        StatusEntity booked = new StatusEntity(1, "BOOKED");
        StatusEntity rented = new StatusEntity(2, "RENTED");
        StatusEntity free = new StatusEntity(3, "FREE");

        booked = statusEntityRepository.save(booked);
        rented = statusEntityRepository.save(rented);
        free = statusEntityRepository.save(free);

        //init OrderTypes
        OrderTypeEntity bookedOrder = new OrderTypeEntity(1, "BOOKED");
        OrderTypeEntity payed = new OrderTypeEntity(2, "PAYED");

        bookedOrder = orderTypeEntityRepository.save(bookedOrder);
        payed = orderTypeEntityRepository.save(payed);

        // FIXME: 5/2/18 Try to save order and person, if ok - show some orders in common view.
    }
}
