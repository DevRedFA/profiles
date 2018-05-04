package com.isedykh.profiles.view;

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

    private TextField type = new TextField("Type");

    private TextField status = new TextField("Status");

    private TextField purchasePrice = new TextField("Purchase price");

    private TextField purchaseDate = new TextField("Purchase date");

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
        verticalLayout.addComponent(pricesGrind);
        pricesGrind.setSelectionMode(Grid.SelectionMode.SINGLE);
        horizontalLayout.addComponent(verticalLayout);
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
        type.setValue(thing.getType().getName());
        status.setValue(thing.getStatus().getName());
        purchasePrice.setValue(String.valueOf(thing.getPurchasePrice()));
        purchaseDate.setValue(thing.getPurchaseDate().toString());
        deposit.setValue(String.valueOf(thing.getDeposit()));

        pricesGrind.setItems(thing.getPrices());
        pricesGrind.addColumn(s -> s.getTerm().getName()).setCaption("Terms");
        pricesGrind.addColumn(Price::getPrice).setCaption("Price");
    }
}
