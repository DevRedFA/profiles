package com.isedykh.profiles.common;

import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.entity.OrderStatusEntity;
import com.isedykh.profiles.dao.entity.PriceEntity;
import com.isedykh.profiles.dao.entity.TermEntity;
import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.dao.entity.ThingStatusEntity;
import com.isedykh.profiles.dao.entity.ThingTypeEntity;
import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import com.isedykh.profiles.dao.repository.OrderEntityRepository;
import com.isedykh.profiles.dao.repository.OrderStatusEntityRepository;
import com.isedykh.profiles.dao.repository.TermEntityRepository;
import com.isedykh.profiles.dao.repository.ThingEntityRepository;
import com.isedykh.profiles.dao.repository.ThingStatusEntityRepository;
import com.isedykh.profiles.dao.repository.ThingTypeEntityRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class InitUtils {

    private final ThingTypeEntityRepository thingTypeEntityRepository;

    private final ThingEntityRepository thingEntityRepository;

    private final ThingStatusEntityRepository thingStatusEntityRepository;

    private final ClientEntityRepository clientEntityRepository;

    private final OrderEntityRepository orderEntityRepository;

    private final OrderStatusEntityRepository orderStatusEntityRepository;

    private final TermEntityRepository termEntityRepository;

    @Transactional
    public List<TermEntity> createTerms() {
        List<TermEntity> result = new ArrayList<>();
        String[] terms = new String[]{"week", "two weeks", "month", "Rentmaina week"};
        Arrays.stream(terms).forEach(term -> result.add(new TermEntity(null, term, 25)));
        return termEntityRepository.saveAll(result);
    }

    @Transactional
    public List<ThingTypeEntity> createThingTypes() {
        List<ThingTypeEntity> list = new ArrayList<>();
        list.add(new ThingTypeEntity(null, "ERGO"));
        list.add(new ThingTypeEntity(null, "SLEEP_BAG"));
        list.add(new ThingTypeEntity(null, "CHILD_CARRIER"));
        list.add(new ThingTypeEntity(null, "CHILD_BIKE_SEAT"));
        list.add(new ThingTypeEntity(null, "CHILD_BIKE_WITH_HAND"));
        return thingTypeEntityRepository.saveAll(list);
    }


    @Transactional
    public List<ThingEntity> updateThings() {
        return thingEntityRepository.findAll();
    }

    @Transactional
    public List<ThingEntity> getThingEntities(List<ThingTypeEntity> thingTypes) {
        List<ThingEntity> listThing = new ArrayList<>();
        ThingStatusEntity free = thingStatusEntityRepository.save(new ThingStatusEntity(null, "free"));
        for (int i = 1; i < 26; i++) {

            listThing.add(new ThingEntity(null, "Thing " + i,
                    i * 100, LocalDate.now(), null, null,
                    thingTypes.get(ThreadLocalRandom.current().nextInt(thingTypes.size())),
                    free, Collections.emptyList(), i, "comments " + i));
        }
        return thingEntityRepository.saveAll(listThing);
    }


    @Transactional
    public void initPrices(List<ThingEntity> listThing, List<TermEntity> terms) {
        for (int i = 1; i < 26; i++) {
            PriceEntity priceWeek = new PriceEntity(null, terms.get(0), i * 100);
            PriceEntity priceTwoWeeks = new PriceEntity(null, terms.get(1), i * 10000);
            PriceEntity priceMonth = new PriceEntity(null, terms.get(2), i * 1000000);
            listThing.get(i - 1).getPrices().add(priceWeek);
            listThing.get(i - 1).getPrices().add(priceTwoWeeks);
            listThing.get(i - 1).getPrices().add(priceMonth);
            thingEntityRepository.save(listThing.get(i - 1));
        }
    }

    @Transactional
    public List<ClientEntity> getClientEntities() {
        List<ClientEntity> listClient = new ArrayList<>();
        for (int i = 1; i < 26; i++) {
            listClient.add(new ClientEntity(null, "name " + i, (long) i, null, Collections.emptyList(), (long) i, "address " + i,
                    i, "children comments " + i, "mail" + i + "@mail.com", "contack link " + i));
        }
        return clientEntityRepository.saveAll(listClient);
    }

    @Transactional
    public List<OrderEntity> getOrderEntities(List<ThingEntity> listThing, List<ClientEntity> listClient) {
        List<OrderEntity> orderEntities = new ArrayList<>();

        OrderStatusEntity booked = orderStatusEntityRepository.save(new OrderStatusEntity(null, "booked"));

        for (int i = 1; i < 26; i++) {
            ThingEntity thing = listThing.get(i - 1);
            ClientEntity client = listClient.get(i - 1);

            OrderEntity e = new OrderEntity(null, "Order comments " + i, Timestamp.valueOf(LocalDate.now().plusDays(i).atStartOfDay()),
                    Timestamp.valueOf(LocalDate.now().plusDays(5L + i).atStartOfDay()), booked, client, thing, thing.getPrices().get(0), null);
            orderEntities.add(e);

            orderEntities.add(new OrderEntity(null, "Order comments " + i, Timestamp.valueOf(LocalDate.now().plusDays(8L + i).atStartOfDay()),
                    Timestamp.valueOf(LocalDate.now().plusDays(15L + i).atStartOfDay()), booked, client, thing, thing.getPrices().get(0), null));
        }
        return orderEntityRepository.saveAll(orderEntities);
    }
}
