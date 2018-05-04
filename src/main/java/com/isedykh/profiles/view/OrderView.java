package com.isedykh.profiles.view;

import com.isedykh.profiles.mapper.OrderMapper;
import com.isedykh.profiles.service.Order;
import com.isedykh.profiles.service.OrderService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@SpringView(name = OrderView.VIEW_NAME)
public class OrderView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "order";

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final OrderMapper orderMapper;

    private Order order;

    private TextField idField = new TextField("Id");

    private TextField status = new TextField("Status");

    private TextField begin = new TextField("Begin");

    private TextField end = new TextField("End");

    private TextField client = new TextField("Client");

    private TextField thing = new TextField("Thing");

    private TextField price = new TextField("Price");

    private TextField comments = new TextField("Comments");

    VerticalLayout verticalLayout = new VerticalLayout();

    HorizontalLayout horizontalLayout = new HorizontalLayout();

    Grid<Order> ordersGrid = new Grid<>();

    @PostConstruct
    public void init() {

        verticalLayout.addComponent(idField);
        verticalLayout.addComponent(status);
        verticalLayout.addComponent(begin);
        verticalLayout.addComponent(end);
        verticalLayout.addComponent(client);
        verticalLayout.addComponent(thing);
        verticalLayout.addComponent(price);
        verticalLayout.addComponent(comments);

        horizontalLayout.addComponent(verticalLayout);

        addComponent(new Label("Detail order view"));
        addComponent(horizontalLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            if (event.getParameters().contains("new")) {
                order = new Order();
                idField.setValue("");

            } else {
                int id = Integer.parseInt(event.getParameters());
                try {
                    order = orderService.findById(id);
                } catch (Exception e) {
                    Notification.show("Client with such idField not found");
                }

            }
        }
        idField.setValue(String.valueOf(order.getId()));
        // TODO: 04.05.2018 change to choosing bar
        status.setValue(String.valueOf(order.getStatus().getName()));
        begin.setValue(String.valueOf(order.getBegin()));
        end.setValue(String.valueOf(order.getEnd()));
        // TODO: 04.05.2018 add fast link to client
        client.setValue(order.getClient().getName());
        // TODO: 04.05.2018 add fast link to thing
        thing.setValue(order.getThing().getName());
        price.setValue(String.valueOf(order.getPrice()));
        comments.setValue(order.getComments());


    }
}
