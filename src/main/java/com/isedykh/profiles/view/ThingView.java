package com.isedykh.profiles.view;

import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.ThingDto;
import com.isedykh.profiles.service.ThingService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lombok.AllArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;

@AllArgsConstructor
@SpringView(name = ThingView.VIEW_NAME)
public class ThingView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "thing";

    private ThingService thingService;

    private ThingMapper thingMapper;

    @PostConstruct
    public void init() {
        addComponent(new Label("All things"));

        List<ThingDto> thingList = thingMapper.ThingsToThingDtos(thingService.getAll());
        Grid<ThingDto> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(thingList);
        grid.addColumn(ThingDto::getName).setCaption("Name");
        grid.addColumn(s -> s.getType().getName()).setCaption("Type");
        grid.addColumn(ThingDto::getDeposit).setCaption("Deposit");
        grid.addColumn(s -> s.getStatus().getName()).setCaption("Status");
        grid.addColumn(ThingDto::getPriceForDay).setCaption("Day");
        grid.addColumn(ThingDto::getPriceForWeek).setCaption("Week");
        grid.addColumn(ThingDto::getPriceForTwoWeeks).setCaption("Two weeks");
        grid.addColumn(ThingDto::getPriceForMonth).setCaption("Month");

        addComponent(grid);
        setExpandRatio(grid, 1f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


}
