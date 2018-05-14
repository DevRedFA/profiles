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
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private ThingEntityRepository thingEntityRepository;

    private PriceEntityRepository priceEntityRepository;

    private ClientEntityRepository clientEntityRepository;

    private OrderEntityRepository orderEntityRepository;

    @Override
    public void run(ApplicationArguments args) {


        //init Thing
        List<ThingEntity> listThing = getThingEntities();


        listThing = updateThings();

        //init Prices
        initPrices(listThing);

        //init Client
        List<ClientEntity> listClient = getClientEntities();

        listThing = updateThings();


        //init Order
        List<OrderEntity> orderEntities = getOrderEntities(listThing, listClient);

        // FIXME: 04.05.2018 nullable params in client and others entities
        // TODO: 5/6/18 Creating and modifying entities.
        // TODO: 5/6/18 images to things profiles
        // TODO: 5/6/18 filtering in greeds!
        // TODO: 5/6/18 drag n drop images
        // TODO: 5/6/18 change things/users/orders to tabs?
        // TODO: 5/6/18 declarative validation in registration form

    }

    @Transactional
    public List<ThingEntity> updateThings() {
        return thingEntityRepository.findAll();
    }

    @Transactional
    public List<ThingEntity> getThingEntities() {
        List<ThingEntity> listThing = new ArrayList<>();
        for (int i = 1; i < 26; i++) {
            listThing.add(new ThingEntity((long) i, "Thing " + i,
                    i * 100, LocalDate.now(), null, "c:/pathToPhoto_" + i,
                    ThingType.ERGO, ThingStatus.FREE, Collections.emptyList(), i, Collections.emptyList(), "comments " + i));
        }
        thingEntityRepository.saveAll(listThing);
        return listThing;
    }

    @Transactional
    public void initPrices(List<ThingEntity> listThing) {
        for (int i = 1; i < 26; i++) {
            PriceEntity priceDay = new PriceEntity((long) (1 + i * 4), Term.DAY, i, listThing.get(i-1));
            PriceEntity priceWeek = new PriceEntity((long) (2 + i * 4), Term.WEEK, i * 100, listThing.get(i-1));
            PriceEntity priceTwoWeeks = new PriceEntity((long) (3 + i * 4), Term.TWO_WEEKS, i * 10000, listThing.get(i-1));
            PriceEntity priceMonth = new PriceEntity((long) (4 + i * 4), Term.MONTH, i * 1000000, listThing.get(i-1));

            priceEntityRepository.save(priceDay);
            priceEntityRepository.save(priceWeek);
            priceEntityRepository.save(priceTwoWeeks);
            priceEntityRepository.save(priceMonth);
        }
    }

    @Transactional
    public List<ClientEntity> getClientEntities() {
        List<ClientEntity> listClient = new ArrayList<>();
        for (int i = 1; i < 26; i++) {
            listClient.add(new ClientEntity((long) i, "name " + i, i, i, "address " + i,
                    i, "children comments " + i, "mail" + i + "@mail.com", "contack link " + i, Collections.emptyList()));
        }
        return clientEntityRepository.saveAll(listClient);
    }

    @Transactional
    public List<OrderEntity> getOrderEntities(List<ThingEntity> listThing, List<ClientEntity> listClient) {
        List<OrderEntity> orderEntities = new ArrayList<>();
        for (int i = 1; i < 26; i++) {
            ThingEntity thing = listThing.get(i - 1);
            ClientEntity client = listClient.get(i - 1);
            OrderEntity e = new OrderEntity((long) i, "Order comments " + i, new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis() + 10000L), OrderStatus.BOOKED, thing, client, thing.getPrices().get(0));
            client.getOrders().add(e);
            thing.getOrders().add(e);
            orderEntities.add(e);
        }
        orderEntityRepository.saveAll(orderEntities);
        return orderEntities;
    }
}
