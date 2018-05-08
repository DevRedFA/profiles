package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.dao.entity.OrderStatus;
import com.isedykh.profiles.dao.entity.ThingStatus;
import com.isedykh.profiles.dao.entity.ThingType;
import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.Price;
import com.isedykh.profiles.service.Thing;
import com.isedykh.profiles.service.ThingDto;
import com.isedykh.profiles.service.ThingService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@RequiredArgsConstructor
@SpringView(name = ThingView.VIEW_NAME)
public class ThingView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "thing";

    @Autowired
    private final ThingService thingService;

    private Thing thing;

    private TextField name = new TextField("Name");

    private ComboBox<ThingType> type = new ComboBox<>("Type");

    private ComboBox<ThingStatus> status = new ComboBox<>("Status");

    private TextField purchasePrice = new TextField("Purchase price");

    private DateField purchaseDate = new DateField("Purchase date");

    private TextField deposit = new TextField("Deposit");

    private VerticalLayout verticalLayout = new VerticalLayout();

    private HorizontalLayout horizontalLayout = new HorizontalLayout();

    private Grid<Price> pricesGrind = new Grid<>();

    @PostConstruct
    public void init() {
        addComponent(new Label("Detail thing view"));
        verticalLayout.addComponent(name);
        verticalLayout.addComponent(type);
        verticalLayout.addComponent(status);
        verticalLayout.addComponent(purchasePrice);
        verticalLayout.addComponent(purchaseDate);
        pricesGrind.setSelectionMode(Grid.SelectionMode.SINGLE);
        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(pricesGrind);
        addComponent(horizontalLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            if (event.getParameters().contains("new")) {
                thing = new Thing();
            } else {
                int id = Integer.parseInt(event.getParameters());
                try {
                    thing = thingService.getById(id);
                } catch (Exception e) {
                    Notification.show("Thing with such id not found");
                }
            }
        }

        Utils.setFieldIfNotNull(thing::getName, name::setValue, s -> s);
        Utils.setFieldIfNotNull(thing::getPurchasePrice, purchasePrice::setValue, String::valueOf);
        Utils.setFieldIfNotNull(thing::getDeposit, deposit::setValue, String::valueOf);
        Utils.setFieldIfNotNull(thing::getPurchaseDate, purchaseDate::setValue, s -> s);

        Utils.setFieldIfNotNull(ThingType::values, type::setItems, s -> s);
        Utils.setFieldIfNotNull(thing::getType, type::setSelectedItem, s -> s);

        Utils.setFieldIfNotNull(ThingStatus::values, status::setItems, s -> s);
        Utils.setFieldIfNotNull(thing::getStatus, status::setSelectedItem, s -> s);

        Utils.setFieldIfNotNull(thing::getPrices, pricesGrind::setItems, s -> s);

        pricesGrind.addColumn(Price::getTerm).setCaption("Terms");
        pricesGrind.addColumn(Price::getPrice).setCaption("Price");
        pricesGrind.setHeightByRows(4);
    }
}
