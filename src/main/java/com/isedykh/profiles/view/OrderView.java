package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.ClientService;
import com.isedykh.profiles.service.OrderService;
import com.isedykh.profiles.service.OrderStatusService;
import com.isedykh.profiles.service.ThingService;
import com.isedykh.profiles.service.entity.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@SpringView(name = OrderView.VIEW_NAME)
public class OrderView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "order";

    private final OrderService orderService;

    private final ClientService clientService;

    private final ThingService thingService;

    private final OrderStatusService orderStatusService;

    private Order order;

    private Label idField = new Label("Id:");

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

    private Grid<Client> clientGrid = new Grid<>();

    private HorizontalLayout horizontalLayout = new HorizontalLayout();

    private HorizontalLayout datesLayout = new HorizontalLayout();

    private HorizontalLayout searchClientPanel = new HorizontalLayout();

    private final HorizontalLayout statusDetails = new HorizontalLayout();

    private Button save = new Button("Save");

    private Button addOrderStatus = new Button("Add order status");

    private TextField nameField = new TextField("Name");

    private Button buttonSearch = new Button("Search");

    private Button addNewClient = new Button("New client");

    private Client client;

    @PostConstruct
    public void init() {

        searchClientPanel.addComponent(nameField);
        searchClientPanel.addComponent(buttonSearch);
        searchClientPanel.setComponentAlignment(buttonSearch, Alignment.BOTTOM_LEFT);
        searchClientPanel.addComponent(addNewClient);
        searchClientPanel.setComponentAlignment(addNewClient, Alignment.BOTTOM_LEFT);

        begin.setDateFormat(Utils.DATE_FORMAT);
        end.setDateFormat(Utils.DATE_FORMAT);

        verticalLayout.addComponent(idField);

        verticalLayout.addComponent(statusDetails);
        statusDetails.addComponent(status);
        statusDetails.addComponent(addOrderStatus);
        statusDetails.setComponentAlignment(addOrderStatus, Alignment.BOTTOM_CENTER);

        verticalLayout.addComponent(datesLayout);
        datesLayout.addComponent(begin);
        datesLayout.addComponent(end);
        verticalLayout.addComponent(clientFiled);
        verticalLayout.addComponent(thingField);
        verticalLayout.addComponent(deposit);
        verticalLayout.addComponent(price);
        verticalLayout.addComponent(comments);
        verticalLayout.addComponent(save);
        comments.setWidth("100%");


        addNewClient.addClickListener(event -> {
            ClientTemplate sub = new ClientTemplate(clientService);
            UI.getCurrent().addWindow(sub);
        });

        grids.addComponent(searchClientPanel);
        grids.addComponent(clientGrid);
        clientGrid.setWidth("100%");

        clientGrid.setItems(clientService.findAll());
        clientGrid.addColumn(Client::getName).setCaption("Name");
        clientGrid.addColumn(Client::getPhone).setCaption("Phone");
        clientGrid.addColumn(Client::getPhoneSecond).setCaption("Second phone");
        clientGrid.addColumn(Client::getEmail).setCaption("Email");
        clientGrid.addColumn(Client::getVkLink).setCaption("Contact link");
        clientGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        clientGrid.setHeightByRows(12);

        clientGrid.addItemClickListener(clickListener -> {
            if (clickListener.getMouseEventDetails().isDoubleClick()) {
                client = clickListener.getItem();
                clientFiled.setValue(client.getName());
                order.setClient(client);
            }
        });

        buttonSearch.addClickListener(event -> {
            String value = nameField.getValue();
            List<Client> clientByName;
            if (!value.isEmpty()) {
                clientByName = new ArrayList<>(clientService.findClientByName(value));
            } else {
                clientByName = clientService.findAll();
            }
            clientGrid.setItems(clientByName);
        });


        addOrderStatus.addClickListener(event -> {
            WindowTemplate<OrderStatus> sub = new WindowTemplate<>(OrderStatus.class, orderStatusService);
            UI.getCurrent().addWindow(sub);
        });

        grids.setWidth("100%");

        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(grids);
        horizontalLayout.setExpandRatio(verticalLayout, 1f);
        horizontalLayout.setExpandRatio(grids, 3f);

        addComponent(horizontalLayout);
        horizontalLayout.setWidth("100%");
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
                        order.setThing(thingService.getById(Long.parseLong(msg.substring(msg.lastIndexOf('=') + 1))));
                    }
                    if (msg.contains("begin")) {
                        order.setBegin(LocalDate.parse(msg.substring(msg.lastIndexOf('=') + 1)));
                    }
                    if (msg.contains("stop")) {
                        order.setStop(LocalDate.parse(msg.substring(msg.lastIndexOf('=') + 1)));
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


        Utils.setFieldIfNotNull(order::getId, idField::setValue, s -> "ID: " + s);
        Utils.setFieldIfNotNull(order::getBegin, begin::setValue, s -> s);
        Utils.setFieldIfNotNull(order::getStop, end::setValue, s -> s);
        Utils.setFieldIfNotNull(order::getStatus, status::setSelectedItem, s -> s);
        Utils.setFieldIfNotNull(orderService::getAllOrderStatuses, status::setItems, s -> s);
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
            order.setPrice(price.getSelectedItem().isPresent() ? price.getSelectedItem().get() : order.getPrice());
            order.setComments(comments.getValue());
            orderService.save(order);
            Notification.show("Order saved");
            getUI().getNavigator().navigateTo(OrdersView.VIEW_NAME);
        });
    }
}
