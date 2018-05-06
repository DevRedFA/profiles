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
                name.setValue("");

            } else {
                int id = Integer.parseInt(event.getParameters());
                try {
                    client = clientService.findById(id);
                } catch (Exception e) {
                    Notification.show("Client with such id not found");
                }

            }
        }
        name.setValue(client.getName());
        phone.setValue(String.valueOf(client.getPhone()));
        phoneSecond.setValue(String.valueOf(client.getPhoneSecond()));
        email.setValue(client.getEmail());
        contactLink.setValue(String.valueOf(client.getContactLink()));
        address.setValue(String.valueOf(client.getAddress()));
        childrenNumber.setValue(String.valueOf(client.getChildrenNumber()));
        childrenComments.setValue(client.getChildrenComments());

        ordersGrid.setItems(client.getOrders());
        ordersGrid.addColumn(s -> s.getThing().getName()).setCaption("Thing");
        ordersGrid.addColumn(Order::getStatus).setCaption("Status");
        ordersGrid.addColumn(Order::getPrice).setCaption("Price");
        ordersGrid.addColumn(Order::getBegin).setCaption("Begin");
        ordersGrid.addColumn(Order::getEnd).setCaption("End");
        ordersGrid.addColumn(Order::getComments).setCaption("Comments");

//        ordersGrid.addItemClickListener(clickEvent -> {
//            if (clickEvent.getMouseEventDetails().isDoubleClick()) {
//                Identifiable order = clickEvent.getItem();
//                String s = OrderView.VIEW_NAME + "/" + order.getId();
//                getUI().getNavigator().navigateTo(s);
//            }
//        });

        ordersGrid.addItemClickListener(this::constructClickListener);
    }

    public void constructClickListener(Grid.ItemClick<? extends Identifiable> clickEvent) {
        if (clickEvent.getMouseEventDetails().isDoubleClick()) {
            Identifiable order = clickEvent.getItem();
            String s = OrderView.VIEW_NAME + "/" + order.getId();
            getUI().getNavigator().navigateTo(s);
        }
    }
}
