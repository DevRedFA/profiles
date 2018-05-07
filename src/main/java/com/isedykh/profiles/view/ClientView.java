package com.isedykh.profiles.view;

import com.isedykh.profiles.mapper.ClientMapper;
import com.isedykh.profiles.service.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
@SpringView(name = ClientView.VIEW_NAME)
public class ClientView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "client";

    @Autowired
    private final ClientService clientService;

    @Autowired
    private final ClientMapper clientMapper;

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

    VerticalLayout verticalLayout = new VerticalLayout();

    HorizontalLayout horizontalLayout = new HorizontalLayout();

    Grid<Order> ordersGrid = new Grid<>();

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

//        name.setValue(client.getName());
        setFieldIfNotNull(client::getName, name::setValue, s -> s);

//        phone.setValue(String.valueOf(client.getPhone()));
        setFieldIfNotNull(client::getPhone, phone::setValue, String::valueOf);

//        phoneSecond.setValue(String.valueOf(client.getPhoneSecond()));
        setFieldIfNotNull(client::getPhoneSecond, phoneSecond::setValue, String::valueOf);

//        email.setValue(client.getEmail());
        setFieldIfNotNull(client::getEmail, email::setValue, s -> s);

//        contactLink.setValue(String.valueOf(client.getContactLink()));
        setFieldIfNotNull(client::getContactLink, contactLink::setValue, String::valueOf);

//        address.setValue(String.valueOf(client.getAddress()));
        setFieldIfNotNull(client::getAddress, address::setValue, s -> s);

//        childrenNumber.setValue(String.valueOf(client.getChildrenNumber()));
        setFieldIfNotNull(client::getChildrenNumber, childrenNumber::setValue, String::valueOf);

//        childrenComments.setValue(client.getChildrenComments());
        setFieldIfNotNull(client::getChildrenComments, childrenComments::setValue, s -> s);

        ordersGrid.setItems(client.getOrders() != null ? client.getOrders() : Collections.emptyList());

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
                clientService.saveClient(client);
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

    private <T, R> void setFieldIfNotNull(Supplier<T> condition, Consumer<R> field, Function<T, R> converter) {
        T t = condition.get();
        if (t != null) {
            field.accept(converter.apply(t));
        }
    }
}
