package com.isedykh.profiles.view;

import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.Identifiable;
import com.isedykh.profiles.service.Thing;
import com.isedykh.profiles.service.ThingDto;
import com.isedykh.profiles.service.ThingService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

import static com.isedykh.profiles.common.Utils.initTailMenu;

@AllArgsConstructor
@SpringView(name = OrdersView.VIEW_NAME)
public class OrdersView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "orders";

    private ThingService thingService;

    private ThingMapper thingMapper;

    @PostConstruct
    public void init() {
        addComponent(new Label("All things"));

        List<Thing> all = thingService.findAll();
        List<ThingDto> thingList = thingMapper.ThingsToThingDtos(all);
        Grid<ThingDto> orderGrid = new Grid<>();
        orderGrid.setSizeFull();
        orderGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        orderGrid.setItems(thingList);
        orderGrid.addColumn(ThingDto::getName).setCaption("Name");
        orderGrid.addColumn(s -> s.getType().getName()).setCaption("Type");
        orderGrid.addColumn(ThingDto::getDeposit).setCaption("Deposit");
        orderGrid.addColumn(s -> s.getStatus().getName()).setCaption("Status");
        orderGrid.addColumn(ThingDto::getPriceForDay).setCaption("Day");
        orderGrid.addColumn(ThingDto::getPriceForWeek).setCaption("Week");
        orderGrid.addColumn(ThingDto::getPriceForTwoWeeks).setCaption("Two weeks");
        orderGrid.addColumn(ThingDto::getPriceForMonth).setCaption("Month");
        addComponent(orderGrid);
        setExpandRatio(orderGrid, 1f);
        orderGrid.addItemClickListener(clickEvent -> {
            if (clickEvent.getMouseEventDetails().isDoubleClick()) {
                ThingDto thingDto = clickEvent.getItem();
                String s = ThingView.VIEW_NAME + "/" + thingDto.getId();
                getUI().getNavigator().navigateTo(s);
            }
        });

//        addComponent(initTailMenu(orderGrid,getUI()));

        HorizontalLayout buttons = new HorizontalLayout();
        Button buttonNext = new Button("Next");
        Button buttonPrivious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNew = new Button("New");

        buttons.addComponent(buttonPrivious);
        buttons.addComponent(buttonDetails);
        buttons.addComponent(buttonNew);
        buttons.addComponent(buttonNext);

        buttonNew.addClickListener(clickEvent -> {
            String state = getUI().getNavigator().getState();
            String s = state.substring(0, state.length() - 1) + "/new";
            getUI().getNavigator().navigateTo(s);
        });

        buttonDetails.addClickListener(clickEvent -> {
            Set selectedItems = orderGrid.getSelectedItems();
            if (selectedItems.size() == 1) {
                Object[] objects = selectedItems.toArray();
                Identifiable obj = Identifiable.class.cast(objects[0]);
                String state = getUI().getNavigator().getState();
                String s = state.substring(0, state.length() - 1) + "/" + obj.getId();
                getUI().getNavigator().navigateTo(s);
            } else {
                Notification.show("Please select one option");
            }
        });

        addComponent(buttons);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


}
