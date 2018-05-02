package com.isedykh.profiles.view;

import com.isedykh.profiles.service.Thing;
import com.isedykh.profiles.service.ThingService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView(name = CommonView.VIEW_NAME)
public class CommonView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "common";

    private ThingService thingService;

    @PostConstruct
    public void init() {
        addComponent(new Label("All things"));

        List<Thing> thingList = thingService.getAll();

        Grid<Thing> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(thingList);
        grid.addColumn(Thing::getName).setCaption("Name");
        grid.addColumn(Thing::getType).setCaption("Type");
        grid.addColumn(Thing::getDeposit).setCaption("Deposit");
        grid.addColumn(Thing::getPrices).setCaption("Prices");

        addComponent(grid);
        setExpandRatio(grid, 1f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
