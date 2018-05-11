package com.isedykh.profiles;

import com.isedykh.profiles.dao.entity.*;
import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import com.isedykh.profiles.dao.repository.OrderEntityRepository;
import com.isedykh.profiles.dao.repository.PriceEntityRepository;
import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private ThingEntityRepository thingEntityRepository;

    private PriceEntityRepository priceEntityRepository;

    private ClientEntityRepository clientEntityRepository;

    private OrderEntityRepository orderEntityRepository;

    @Override
    public void run(ApplicationArguments args) {


        //init Thing
        List<ThingEntity> listThing = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            listThing.add(new ThingEntity(i, "Thing " + i,
                    i * 100, LocalDate.now(), null, "c:/pathToPhoto_" + i, ThingType.ERGO, ThingStatus.FREE, Collections.emptyList(), i, "comments "+ i));
        }
        thingEntityRepository.saveAll(listThing);

        //init Prices
        for (int i = 0; i < 25; i++) {
            PriceEntity priceDay = new PriceEntity(1 + i*4, Term.DAY, i, listThing.get(i));
            PriceEntity priceWeek = new PriceEntity(2+ i*4, Term.WEEK, i*100, listThing.get(i));
            PriceEntity priceTwoWeeks = new PriceEntity(3+ i*4, Term.TWO_WEEKS, i*10000, listThing.get(i));
            PriceEntity priceMonth = new PriceEntity(4+ i*4, Term.MONTH, i*1000000, listThing.get(i));

            priceEntityRepository.save(priceDay);
            priceEntityRepository.save(priceWeek);
            priceEntityRepository.save(priceTwoWeeks);
            priceEntityRepository.save(priceMonth);
        }

        //init Client
        List<ClientEntity> listClient = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            listClient.add(new ClientEntity(i, "name " + i, i, i, "address " + i,
                    i, "children comments " + i, "mail" + i + "@mail.com", "contack link " + i, Collections.emptyList()));
        }
        clientEntityRepository.saveAll(listClient);

        //init Order
        List<OrderEntity> orderEntities = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            orderEntities.add(new OrderEntity(i, "Order comments " + i, new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis() + 10000L), OrderStatus.BOOKED, listThing.get(i), listClient.get(i), listThing.get(i).getPrices().get(0)));
        }
        orderEntityRepository.saveAll(orderEntities);

        // FIXME: 04.05.2018 nullable params in client and others entities
        // TODO: 5/6/18 Creating and modifying entities.
        // TODO: 5/6/18 images to things profiles
        // TODO: 5/6/18 filtering in greeds!
        // TODO: 5/6/18 drag n drop images
        // TODO: 5/6/18 change things/users/orders to tabs?
        // TODO: 5/6/18 declarative validation in registration form

    }
}
