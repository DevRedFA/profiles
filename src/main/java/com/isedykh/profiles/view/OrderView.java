package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.dao.entity.OrderStatus;
import com.isedykh.profiles.mapper.OrderMapper;
import com.isedykh.profiles.service.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

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

    private TextField client = new TextField("Client");

    private TextField thing = new TextField("Thing");

    private TextField price = new TextField("Price");

    private TextArea comments = new TextArea("Comments");

    private VerticalLayout verticalLayout = new VerticalLayout();

    private HorizontalLayout horizontalLayout = new HorizontalLayout();

    private Button save = new Button("Save");

    @PostConstruct
    public void init() {

        verticalLayout.addComponent(idField);
        verticalLayout.addComponent(status);
        verticalLayout.addComponent(begin);
        verticalLayout.addComponent(end);
        verticalLayout.addComponent(client);
        verticalLayout.addComponent(thing);
        verticalLayout.addComponent(price);
        verticalLayout.addComponent(save);

        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(comments);


        addComponent(new Label("Detail order view"));
        addComponent(horizontalLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            if (event.getParameters().contains("new")) {
                order = new Order();
            } else {
                int id = Integer.parseInt(event.getParameters());
                try {
                    order = orderService.findById(id);
                } catch (Exception e) {
                    Notification.show("Client with such idField not found");
                }
            }
        }

        Utils.setFieldIfNotNull(order::getId, idField::setValue, String::valueOf);
        Utils.setFieldIfNotNull(order::getBegin, begin::setValue, s -> s);
        Utils.setFieldIfNotNull(order::getEnd, end::setValue, s -> s);
        Utils.setFieldIfNotNull(order::getStatus, status::setSelectedItem, s -> s);
        Utils.setFieldIfNotNull(OrderStatus::values, status::setItems, s -> s);
        Utils.setFieldIfNotNull(order::getPrice, price::setValue, String::valueOf);
        Utils.setFieldIfNotNull(order::getComments, comments::setValue, s -> s);

        // TODO: 04.05.2018 add fast link to client
        Utils.setFieldIfNotNull(order::getClient, client::setValue, Client::getName);

        // TODO: 04.05.2018 add fast link to thing
        Utils.setFieldIfNotNull(order::getThing, thing::setValue, Thing::getName);

        status.setSelectedItem(order.getStatus());

        client.addValueChangeListener(changedListener -> {
            String value = changedListener.getValue();
            List<Client> clientByName = clientService.findClientByName(value);
            if (clientByName.isEmpty()) {
                Notification.show("Client not found");
            }
            if (clientByName.size() != 1) {
                Notification.show("Several clients found");
            }
            order.setClient(clientByName.get(0));
        });

        thing.addValueChangeListener(changedListener -> {
            String value = changedListener.getValue();
            List<Thing> allByName = thingService.findAllByName(value);
            if (allByName.isEmpty()) {
                Notification.show("Client not found");
            }
            if (allByName.size() != 1) {
                Notification.show("Several clients found");
            }
            order.setThing(allByName.get(0));
        });


        save.addClickListener(clickEvent -> {
            order.setId(Long.parseLong(idField.getValue()));
            order.setBegin(begin.getValue());
            order.setEnd(end.getValue());
            order.setStatus(status.getSelectedItem().get());
            // FIXME: 08.05.2018 Change cents to rubles
//            order.setPrice(price);
            order.setComments(comments.getValue());
            orderService.save(order);
        });
    }
}
