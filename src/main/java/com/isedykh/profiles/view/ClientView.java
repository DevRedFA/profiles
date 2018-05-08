package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.ClientService;
import com.isedykh.profiles.service.Identifiable;
import com.isedykh.profiles.service.Order;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@SpringView(name = ClientView.VIEW_NAME)
public class ClientView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "client";

    @Autowired
    private final ClientService clientService;

    private Client client;

    private TextField name = new TextField("Name");

    private TextField phone = new TextField("Phone");

    private TextField phoneSecond = new TextField("Second phone");

    private TextField address = new TextField("Address");

    private TextField childrenNumber = new TextField("Children number");

    private TextField email = new TextField("Email");

    private TextField contactLink = new TextField("Contact link");

    private TextArea childrenComments = new TextArea("Children comments");

    private Button save = new Button("Save");

    private VerticalLayout verticalLayout = new VerticalLayout();

    private HorizontalLayout horizontalLayout = new HorizontalLayout();

    private Grid<Order> ordersGrid = new Grid<>();

    @PostConstruct
    public void init() {

        verticalLayout.addComponent(name);
        verticalLayout.addComponent(phone);
        verticalLayout.addComponent(phoneSecond);
        verticalLayout.addComponent(email);
        verticalLayout.addComponent(contactLink);
        verticalLayout.addComponent(address);
        verticalLayout.addComponent(childrenNumber);
        verticalLayout.addComponent(save);

        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(childrenComments);

        addComponent(new Label("Detail client view"));
        addComponent(horizontalLayout);
        addComponent(ordersGrid);
        ordersGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        ordersGrid.setSizeFull();
        setExpandRatio(ordersGrid, 1f);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            if (event.getParameters().contains("new")) {
                client = new Client();
            } else {
                int id = Integer.parseInt(event.getParameters());
                try {
                    client = clientService.findById(id);
                } catch (Exception e) {
                    Notification.show("Client with such id not found");
                }
            }
        }

        Utils.setFieldIfNotNull(client::getName, name::setValue, s -> s);

        Utils.setFieldIfNotNull(client::getPhone, phone::setValue, String::valueOf);

        Utils.setFieldIfNotNull(client::getPhoneSecond, phoneSecond::setValue, String::valueOf);

        Utils.setFieldIfNotNull(client::getEmail, email::setValue, s -> s);

        Utils.setFieldIfNotNull(client::getContactLink, contactLink::setValue, String::valueOf);

        Utils.setFieldIfNotNull(client::getAddress, address::setValue, s -> s);

        Utils.setFieldIfNotNull(client::getChildrenNumber, childrenNumber::setValue, String::valueOf);

        Utils.setFieldIfNotNull(client::getChildrenComments, childrenComments::setValue, s -> s);

        Utils.setFieldIfNotNull(client::getOrders, ordersGrid::setItems, s -> s);

        ordersGrid.addColumn(s -> s.getThing().getName()).setCaption("Thing");
        ordersGrid.addColumn(Order::getStatus).setCaption("Status");
        ordersGrid.addColumn(Order::getPrice).setCaption("Price");
        ordersGrid.addColumn(Order::getBegin).setCaption("Begin");
        ordersGrid.addColumn(Order::getEnd).setCaption("End");
        ordersGrid.addColumn(Order::getComments).setCaption("Comments");

        ordersGrid.addItemClickListener(this::constructClickListener);

        save.addClickListener(clickEvent -> {
            client.setName(name.getValue());
            try {
                client.setPhone(Long.parseLong(phone.getValue()));
                client.setPhoneSecond(Long.parseLong(phoneSecond.getValue()));
                client.setEmail(email.getValue());
                client.setContactLink(contactLink.getValue());
                client.setAddress(address.getValue());
                client.setChildrenNumber(Integer.parseInt(childrenNumber.getValue()));
                client.setChildrenComments(childrenComments.getValue());
                clientService.save(client);
            } catch (NumberFormatException e) {
                Notification.show("Bad format of phone/second phone");
            }
        });
    }

    public void constructClickListener(Grid.ItemClick<? extends Identifiable> clickEvent) {
        if (clickEvent.getMouseEventDetails().isDoubleClick()) {
            Identifiable order = clickEvent.getItem();
            String s = OrderView.VIEW_NAME + "/" + order.getId();
            getUI().getNavigator().navigateTo(s);
        }
    }


}
