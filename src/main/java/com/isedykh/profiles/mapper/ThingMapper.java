package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ThingEntity;
import com.isedykh.profiles.service.Price;
import com.isedykh.profiles.service.Term;
import com.isedykh.profiles.service.Thing;
import com.isedykh.profiles.service.ThingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThingMapper {

    Thing ThingEntityToThing(ThingEntity ThingEntity);

    ThingEntity ThingToThingEntity(Thing Thing);

    List<Thing> ThingEntitiesToThings(List<ThingEntity> ThingEntities);

    List<ThingEntity> ThingsToThingEntities(List<Thing> Thing);


    //shitty decision, but now have no better idea
    default ThingDto ThingToThingDto(Thing thing) {

        List<Price> prices = thing.getPrices();
        final Price[] day = new Price[1];
        final Price[] week = new Price[1];
        final Price[] twoWeeks = new Price[1];
        final Price[] month = new Price[1];
        prices.forEach(s -> {
            if (s.getTerm().equals(new Term("DAY"))) {
                day[0] = s;
            }
            if (s.getTerm().equals(new Term("WEEK"))) {
                week[0] = s;
            }
            if (s.getTerm().equals(new Term("TWO_WEEKS"))) {
                twoWeeks[0] = s;
            }
            if (s.getTerm().equals(new Term("MONTH"))) {
                month[0] = s;
            }
        });

        return ThingDto.builder().name(thing.getName())
                .deposit(thing.getDeposit())
                .pathToPhoto(thing.getPathToPhoto())
                .photo(thing.getPhoto())
                .purchaseDate(thing.getPurchaseDate())
                .purchasePrice(thing.getPurchasePrice())
                .type(thing.getType())
                .status(thing.getStatus())
                .priceForDay(day[0].getPrice())
                .priceForWeek(week[0].getPrice())
                .priceForTwoWeeks(twoWeeks[0].getPrice())
                .priceForMonth(month[0].getPrice())
                .build();
    }

    List<ThingDto> ThingsToThingDtos(List<Thing> things);

}
