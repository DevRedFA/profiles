package com.isedykh.profiles;

import com.isedykh.profiles.dao.entity.*;
import com.isedykh.profiles.dao.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private ThingTypeEntityRepository thingTypeEntityRepository;

    private ThingEntityRepository thingEntityRepository;

    private TermEntityRepository termEntityRepository;

    private PriceEntityRepository priceEntityRepository;

    private OrderStatusEntityRepository orderStatusEntityRepository;

    private ThingStatusEntityRepository thingStatusEntityRepository;

    private PersonEntityRepository personEntityRepository;

    private OrderEntityRepository orderEntityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //init ThingTypes
        ThingTypeEntity ergo = thingTypeEntityRepository.save(new ThingTypeEntity(1, "ERGO"));
        thingTypeEntityRepository.save(new ThingTypeEntity(2, "SLEEPBAG"));
        thingTypeEntityRepository.save(new ThingTypeEntity(3, "CHILD_CARRIER"));

        //init Term

        TermEntity day = termEntityRepository.save(new TermEntity(1, "DAY"));
        TermEntity week = termEntityRepository.save(new TermEntity(2, "WEEK"));
        TermEntity twoWeeks = termEntityRepository.save(new TermEntity(3, "TWO WEEKS"));
        TermEntity month = termEntityRepository.save(new TermEntity(4, "MONTH"));

        //init thingStatus
        ThingStatusEntity repairedThing = new ThingStatusEntity(1, "REPAIRED");
        ThingStatusEntity rentedThing = new ThingStatusEntity(2, "RENTED");
        ThingStatusEntity bookedThing = new ThingStatusEntity(2, "BOOKED");
        ThingStatusEntity freeThing = new ThingStatusEntity(2, "FREE");

        repairedThing = thingStatusEntityRepository.save(repairedThing);
        rentedThing = thingStatusEntityRepository.save(rentedThing);
        bookedThing = thingStatusEntityRepository.save(bookedThing);
        freeThing = thingStatusEntityRepository.save(freeThing);


        //init Thing
        ThingEntity thing = new ThingEntity(1, "Thing 1",
                1000, LocalDate.now(), null, "c:/pathToPhoto", ergo, freeThing, Collections.EMPTY_LIST, 300);
        thing = thingEntityRepository.save(thing);

        //init Prices
        PriceEntity priceDay = new PriceEntity(1, day, 100, thing);
        PriceEntity priceWeek = new PriceEntity(2, week, 300, thing);
        PriceEntity priceTwoWeeks = new PriceEntity(3, twoWeeks, 500, thing);
        PriceEntity priceMonth = new PriceEntity(4, month, 1000, thing);

        priceEntityRepository.save(priceDay);
        priceEntityRepository.save(priceWeek);
        priceEntityRepository.save(priceTwoWeeks);
        priceEntityRepository.save(priceMonth);

        //init OrderStatus
        OrderStatusEntity rejected = new OrderStatusEntity(1, "REJECTED");
        OrderStatusEntity rented = new OrderStatusEntity(2, "RENTED");
        OrderStatusEntity closed = new OrderStatusEntity(3, "CLOSED");
        OrderStatusEntity booked = new OrderStatusEntity(3, "BOOKED");

        rejected = orderStatusEntityRepository.save(rejected);
        rented = orderStatusEntityRepository.save(rented);
        closed = orderStatusEntityRepository.save(closed);
        booked = orderStatusEntityRepository.save(booked);


        //init Client
        ClientEntity person = new ClientEntity(3, "name 3", 3333333333L, 44444444444L, "address 3",
                4, "children comments 3", "mail3@mail.com", "contack link 3", Collections.emptyList());
        person = personEntityRepository.save(person);

        //init Order
        OrderEntity order = new OrderEntity(1, "Order comments 2", new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis() + 10000L), booked, thing, person, 1000);
        OrderEntity save = orderEntityRepository.save(order);


        Optional<ThingEntity> byId = thingEntityRepository.findById(1L);
        Thread.sleep(100);

        // TODO: 5/3/18 new view: detal for thing page
        // FIXME: 04.05.2018 nullable params in client and others entities
    }
}
