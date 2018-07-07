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
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.isedykh.profiles.view.ViewUtils.getClientGridWithSettings;

@RequiredArgsConstructor
@SpringView(name = OrderView.VIEW_NAME)
@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
public class OrderView extends VerticalLayout implements View {

    static final String VIEW_NAME = "order";

    private final OrderService orderService;
    private final ClientService clientService;
    private final ThingService thingService;
    private final OrderStatusService orderStatusService;

    private Order order;
    private Client client;

    private Label idField = new Label("Id:");
    private TextField nameField = new TextField("Name");
    private ComboBox<OrderStatus> status = new ComboBox<>("Status");
    private TextField clientFiled = new TextField("Client");
    private TextField thingField = new TextField("Thing");
    private ComboBox<Price> price = new ComboBox<>("Price");
    private TextField actualPriceField = new TextField("Actual price");
    private TextField deposit = new TextField("Deposit");
    private TextArea comments = new TextArea("Comments");
    private DateField begin = new DateField("Begin");
    private DateField end = new DateField("End");
    private Grid<Client> clientGrid = getClientGridWithSettings();


    @PostConstruct
    public void init() {

        Button addOrderStatus = new Button("Add order status");
        addOrderStatus.addClickListener(event -> {
            WindowTemplate<OrderStatus> sub = new WindowTemplate<>(OrderStatus.class, orderStatusService);
            UI.getCurrent().addWindow(sub);
        });

        Button buttonSearch = new Button("Search");
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

        Button addNewClient = new Button("New client");
        addNewClient.addClickListener(event -> {
            ClientTemplate sub = new ClientTemplate(clientService);
            UI.getCurrent().addWindow(sub);
        });

        Button save = new Button("Save");
        save.addClickListener(clickEvent -> {
            order.setBegin(begin.getValue());
            order.setStop(end.getValue());
            if (!StringUtils.isEmpty(actualPriceField.getValue())) {
                order.setActualPrice(Integer.valueOf(actualPriceField.getValue()) * 100);
            } else {
                order.setActualPrice(price.getSelectedItem().isPresent() ?
                        price.getSelectedItem().get().getPriceValue()
                        : order.getPrice().getPriceValue());
            }
            order.setStatus(status.getSelectedItem().isPresent() ? status.getSelectedItem().get() : order.getStatus());
            order.setPrice(price.getSelectedItem().isPresent() ? price.getSelectedItem().get() : order.getPrice());
            order.setComments(comments.getValue());
            orderService.save(order);
            Notification.show("Order saved");
            getUI().getNavigator().navigateTo(OrdersView.VIEW_NAME);
        });

        begin.setDateFormat(Utils.DATE_FORMAT);
        end.setDateFormat(Utils.DATE_FORMAT);

        price.setWidth("100%");
        comments.setWidth("100%");

        clientGrid.setHeightByRows(12);
        clientGrid.setItems(clientService.findAll());
        clientGrid.addItemClickListener(clickListener -> {
            if (clickListener.getMouseEventDetails().isDoubleClick()) {
                client = clickListener.getItem();
                clientFiled.setValue(client.getName());
                order.setClient(client);
            }
        });

        HorizontalLayout statusDetails = new HorizontalLayout();
        statusDetails.addComponent(status);
        statusDetails.addComponent(addOrderStatus);
        statusDetails.setComponentAlignment(addOrderStatus, Alignment.BOTTOM_CENTER);

        HorizontalLayout datesLayout = new HorizontalLayout();
        datesLayout.addComponent(begin);
        datesLayout.addComponent(end);

        HorizontalLayout pricesPanel = new HorizontalLayout();
        pricesPanel.addComponent(deposit);
        pricesPanel.addComponent(actualPriceField);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(idField);
        verticalLayout.addComponent(statusDetails);
        verticalLayout.addComponent(datesLayout);
        verticalLayout.addComponent(clientFiled);
        verticalLayout.addComponent(thingField);
        verticalLayout.addComponent(price);
        verticalLayout.addComponent(pricesPanel);
        verticalLayout.addComponent(comments);
        verticalLayout.addComponent(save);

        HorizontalLayout searchClientPanel = new HorizontalLayout();
        searchClientPanel.addComponent(nameField);
        searchClientPanel.addComponent(buttonSearch);
        searchClientPanel.setComponentAlignment(buttonSearch, Alignment.BOTTOM_LEFT);
        searchClientPanel.addComponent(addNewClient);
        searchClientPanel.setComponentAlignment(addNewClient, Alignment.BOTTOM_LEFT);

        VerticalLayout grids = new VerticalLayout();
        grids.addComponent(searchClientPanel);
        grids.addComponent(clientGrid);
        grids.setWidth("100%");

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.addComponent(verticalLayout);
        mainLayout.addComponent(grids);
        mainLayout.setExpandRatio(verticalLayout, 1f);
        mainLayout.setExpandRatio(grids, 3f);
        mainLayout.setWidth("100%");

        addComponent(mainLayout);
        setExpandRatio(mainLayout, 1f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        if (event.getParameters() != null) {
            String[] msgs = event.getParameters().split("/");
            if (event.getParameters().contains("new")) {
                order = new Order();
                parseRequestParams(msgs);
            } else {
                try {
                    int id = Integer.parseInt(event.getParameters());
                    order = orderService.findById(id);
                    client = order.getClient();
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
        Utils.setFieldIfNotNull(order::getActualPrice, actualPriceField::setValue, s -> String.valueOf(s / 100));
        Utils.setFieldIfNotNull(order::getThing, deposit::setValue, s -> String.valueOf(s.getDeposit()));
        Utils.setFieldIfNotNull(order::getComments, comments::setValue, s -> s);

        if (client != null) {
            Utils.setFieldIfNotNull(client::getName, clientFiled::setValue, s -> s);
        }

        Utils.setFieldIfNotNull(order::getThing, thingField::setValue, Thing::getName);
    }

    private void parseRequestParams(String[] msgs) {
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
    }
}
