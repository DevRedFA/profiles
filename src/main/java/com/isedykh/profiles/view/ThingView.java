package com.isedykh.profiles.view;

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

    @Autowired
    private final ThingMapper thingMapper;

    private Thing thing;

    private TextField name = new TextField("Name");

    private ComboBox<ThingType> type = new ComboBox<>("Type");

    private ComboBox<ThingStatus> status = new ComboBox<>("Status");

    private TextField purchasePrice = new TextField("Purchase price");

    private DateField purchaseDate = new DateField("Purchase date");

    private TextField deposit = new TextField("Deposit");

    VerticalLayout verticalLayout = new VerticalLayout();

    HorizontalLayout horizontalLayout = new HorizontalLayout();

    Grid<Price> pricesGrind = new Grid<>();

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
                name.setValue("");

            } else {
                int id = Integer.parseInt(event.getParameters());
                try {
                    thing = thingService.getById(id);
                } catch (Exception e) {
                    Notification.show("Thing with such id not found");
                }

            }
        }
        name.setValue(thing.getName());

        type.setItems(ThingType.values());
        type.setSelectedItem(thing.getType());

        status.setItems(ThingStatus.values());
        status.setSelectedItem(thing.getStatus());

        purchasePrice.setValue(String.valueOf(thing.getPurchasePrice()));
        purchaseDate.setValue(thing.getPurchaseDate());
        deposit.setValue(String.valueOf(thing.getDeposit()));

        pricesGrind.setItems(thing.getPrices());
        pricesGrind.addColumn(Price::getTerm).setCaption("Terms");
        pricesGrind.addColumn(Price::getPrice).setCaption("Price");
        pricesGrind.setHeightByRows(4);
    }
}
