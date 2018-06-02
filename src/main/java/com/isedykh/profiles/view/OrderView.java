package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.dao.entity.OrderStatus;
import com.isedykh.profiles.service.*;
import com.isedykh.profiles.service.entity.Client;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.entity.Price;
import com.isedykh.profiles.service.entity.Thing;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@RequiredArgsConstructor
@SpringView(name = OrderView.VIEW_NAME)
public class OrderView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "order";

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final ClientService clientService;

    @Autowired
    private final ThingService thingService;

    private Order order;

    private TextField idField = new TextField("Id");

    private ComboBox<OrderStatus> status = new ComboBox<>("Status");

    private DateField begin = new DateField("Begin");

    private DateField end = new DateField("End");

    private TextField clientFiled = new TextField("Client");

    private TextField thingField = new TextField("Thing");

    private ComboBox<Price> price = new ComboBox<>("Price");

    private TextField deposit = new TextField("Deposit");

    private TextArea comments = new TextArea("Comments");

    private VerticalLayout verticalLayout = new VerticalLayout();

    private VerticalLayout grids = new VerticalLayout();

    Grid<Thing> thingsGrid = new Grid<>();

    Grid<Client> clientGrid = new Grid<>();

    private HorizontalLayout horizontalLayout = new HorizontalLayout();

    private Button save = new Button("Save");

    Client client;

    @PostConstruct
    public void init() {

        begin.setDateFormat(Utils.DATE_FORMAT);
        end.setDateFormat(Utils.DATE_FORMAT);

        verticalLayout.addComponent(idField);
        verticalLayout.addComponent(status);
        verticalLayout.addComponent(begin);
        verticalLayout.addComponent(end);
        verticalLayout.addComponent(clientFiled);
        verticalLayout.addComponent(thingField);
        verticalLayout.addComponent(deposit);
        verticalLayout.addComponent(price);
        verticalLayout.addComponent(save);

        grids.addComponent(thingsGrid);
        thingsGrid.setItems(thingService.findAll());
        thingsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        thingsGrid.addColumn(Thing::getName).setCaption("Name");
        thingsGrid.addColumn(Thing::getType).setCaption("Type");
        thingsGrid.addColumn(Thing::getDeposit).setCaption("Deposit");
        thingsGrid.addColumn(Thing::getStatus).setCaption("Status");
        thingsGrid.setHeightByRows(6);

        thingsGrid.addItemClickListener(clickListener -> {
            if (clickListener.getMouseEventDetails().isDoubleClick()) {
                Thing thing = clickListener.getItem();
                order.setThing(thing);
                thingField.setValue(thing.getName());
                price.setItems(thing.getPrices());
                price.setSelectedItem(thing.getPrices().get(0));
            }
        });

        grids.addComponent(clientGrid);
        clientGrid.setItems(clientService.findAll());
        clientGrid.addColumn(Client::getName).setCaption("Name");
        clientGrid.addColumn(Client::getPhone).setCaption("Phone");
        clientGrid.addColumn(Client::getPhoneSecond).setCaption("Second phone");
        clientGrid.addColumn(Client::getEmail).setCaption("Email");
        clientGrid.addColumn(Client::getVkLink).setCaption("Contact link");
        clientGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        clientGrid.setHeightByRows(6);

        clientGrid.addItemClickListener(clickListener -> {
            if (clickListener.getMouseEventDetails().isDoubleClick()) {
                client = clickListener.getItem();
                clientFiled.setValue(client.getName());
                order.setClient(client);
            }
        });

        grids.setExpandRatio(clientGrid, 1f);
        grids.setExpandRatio(thingsGrid, 1f);

        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(comments);
        horizontalLayout.addComponent(grids);
        horizontalLayout.setExpandRatio(grids, 1f);

        addComponent(new Label("Detail order view"));
        addComponent(horizontalLayout);
        setExpandRatio(horizontalLayout, 1f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        if (event.getParameters() != null) {
            String[] msgs = event.getParameters().split("/");
            if (event.getParameters().contains("new")) {
                order = new Order();
                for (String msg : msgs) {
                    if (msg.contains("thing")) {
                        order.setThing(thingService.getById(Long.parseLong(msg.substring(msg.lastIndexOf("=")+1))));
                    }
                    if (msg.contains("begin")) {
                        order.setBegin(LocalDate.parse(msg.substring(msg.lastIndexOf("=")+1)));
                    }
                    if (msg.contains("stop")) {
                        order.setStop(LocalDate.parse(msg.substring(msg.lastIndexOf("=")+1)));
                    }
                }
            } else {
                try {
                    int id = Integer.parseInt(event.getParameters());
                    order = orderService.findById(id);
                    client = order.getClient();
                    // TODO: 04.05.2018 add fast link to clientFiled

                } catch (Exception e) {
                    Notification.show("Client with such idField not found");
                }
            }
        }

        Utils.setFieldIfNotNull(order::getId, idField::setValue, String::valueOf);
        Utils.setFieldIfNotNull(order::getBegin, begin::setValue, s -> s);
        Utils.setFieldIfNotNull(order::getStop, end::setValue, s -> s);
        Utils.setFieldIfNotNull(order::getStatus, status::setSelectedItem, s -> s);
        Utils.setFieldIfNotNull(OrderStatus::values, status::setItems, s -> s);
        Utils.setFieldIfNotNull(order::getPrice, price::setSelectedItem, s -> s);
        Utils.setFieldIfNotNull(order::getThing, price::setItems, Thing::getPrices);
        Utils.setFieldIfNotNull(order::getThing, deposit::setValue, s -> String.valueOf(s.getDeposit()));
        Utils.setFieldIfNotNull(order::getComments, comments::setValue, s -> s);

        if (client != null) {
            Utils.setFieldIfNotNull(client::getName, clientFiled::setValue, s -> s);
        }

        // TODO: 04.05.2018 add fast link to thingField
        Utils.setFieldIfNotNull(order::getThing, thingField::setValue, Thing::getName);

        save.addClickListener(clickEvent -> {
            order.setBegin(begin.getValue());
            order.setStop(end.getValue());
            order.setStatus(status.getSelectedItem().isPresent() ? status.getSelectedItem().get() : order.getStatus());
            // FIXME: 08.05.2018 Change cents to rubles
            order.setPrice(price.getSelectedItem().isPresent() ? price.getSelectedItem().get() : order.getPrice());
            order.setComments(comments.getValue());
            orderService.save(order);
            Notification.show("Thing saved");
            getUI().getNavigator().navigateTo(OrdersView.VIEW_NAME);
        });
    }
}
