package com.isedykh.profiles.view;

import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.*;
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
@SpringView(name = ClientsView.VIEW_NAME)
public class ClientsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "clients";

    private ClientService clientService;

    private ThingMapper thingMapper;

    @PostConstruct
    public void init() {
        addComponent(new Label("All clients"));

        List<Client> clients = clientService.findAll();
        Grid<Client> clientGrid = new Grid<>();
        clientGrid.setSizeFull();
        clientGrid.setItems(clients);
        clientGrid.addColumn(Client::getName).setCaption("Name");
        clientGrid.addColumn(Client::getPhone).setCaption("Phone");
        clientGrid.addColumn(Client::getPhoneSecond).setCaption("Second phone");
        clientGrid.addColumn(Client::getEmail).setCaption("Email");
        clientGrid.addColumn(Client::getContactLink).setCaption("Contact link");
        clientGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        addComponent(clientGrid);
        setExpandRatio(clientGrid, 1f);
        clientGrid.addItemClickListener(clickEvent -> {
            if (clickEvent.getMouseEventDetails().isDoubleClick()) {
                Client client = clickEvent.getItem();
                String s = ClientView.VIEW_NAME + "/" + client.getId();
                getUI().getNavigator().navigateTo(s);
            }
        });

//        addComponent(initTailMenu(clientGrid,getUI()));

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
            Set selectedItems = clientGrid.getSelectedItems();
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
