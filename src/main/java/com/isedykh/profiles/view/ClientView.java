package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.*;
import com.isedykh.profiles.service.entity.Client;
import com.isedykh.profiles.service.entity.Identifiable;
import com.isedykh.profiles.service.entity.Order;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@SpringView(name = ClientView.VIEW_NAME)
public class ClientView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "client";

    @Autowired
    private final ClientService clientService;

    @Autowired
    private final OrderService orderService;

    private Client client;

    private TextField name = new TextField("Name");

    private TextField phone = new TextField("Phone");

    private TextField phoneSecond = new TextField("Second phone");

    private TextField address = new TextField("Address");

    private TextField childrenNumber = new TextField("Children number");

    private TextField email = new TextField("Email");

    private TextField vkLink = new TextField("VK link");

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
        verticalLayout.addComponent(vkLink);
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
                    Utils.setFieldIfNotNull(client::getName, name::setValue, s -> s);

                    Utils.setFieldIfNotNull(client::getPhone, phone::setValue, String::valueOf);

                    Utils.setFieldIfNotNull(client::getPhoneSecond, phoneSecond::setValue, String::valueOf);

                    Utils.setFieldIfNotNull(client::getEmail, email::setValue, s -> s);

                    Utils.setFieldIfNotNull(client::getVkLink, vkLink::setValue, String::valueOf);

                    Utils.setFieldIfNotNull(client::getAddress, address::setValue, s -> s);

                    Utils.setFieldIfNotNull(client::getChildrenNumber, childrenNumber::setValue, String::valueOf);

                    Utils.setFieldIfNotNull(client::getChildrenComments, childrenComments::setValue, s -> s);

                    List<Order> clientOrderHistory = orderService.getClientOrderHistory(client);

                    Utils.setFieldIfNotNull(clientOrderHistory::stream, ordersGrid::setItems, s -> s);

                } catch (Exception e) {
                    Notification.show("Client with such id not found");
                }
            }
        }


        ordersGrid.addColumn(s -> s.getThing().getName()).setCaption("Thing");
        ordersGrid.addColumn(Order::getStatus).setCaption("Status");
        ordersGrid.addColumn(Order::getPrice).setCaption("Price");
        ordersGrid.addColumn(Order::getBegin).setCaption("Begin");
        ordersGrid.addColumn(Order::getStop).setCaption("End");
        ordersGrid.addColumn(Order::getComments).setCaption("Comments");

        ordersGrid.addItemClickListener(this::constructClickListener);

        save.addClickListener(clickEvent -> {
            boolean containsContact = false;

            client.setName(name.getValue());

            try {
                if (!StringUtils.isBlank(phone.getValue())) {
                    client.setPhone(Long.parseLong(phone.getValue()));
                    containsContact = true;
                    phone.setComponentError(null);
                }
            } catch (NumberFormatException e) {
                phone.setComponentError(new UserError("Bad format of phone"));
            }

            try {
                if (!StringUtils.isBlank(phoneSecond.getValue())) {
                    client.setPhoneSecond(Long.parseLong(phoneSecond.getValue()));
                    containsContact = true;
                    phoneSecond.setComponentError(null);
                }
            } catch (NumberFormatException e) {
                phoneSecond.setComponentError(new UserError("Bad format of second phone"));
            }

            if (!StringUtils.isBlank(phoneSecond.getValue())) {
                client.setEmail(email.getValue());
                containsContact = true;
            }

            if (!StringUtils.isBlank(vkLink.getValue())) {
                client.setVkLink(vkLink.getValue());
                containsContact = true;
            }

            if (!StringUtils.isBlank(address.getValue())) {
                client.setAddress(address.getValue());
            }
            try {
                if (!StringUtils.isBlank(childrenNumber.getValue())) {
                    client.setChildrenNumber(Integer.parseInt(childrenNumber.getValue()));
            }
            } catch (NumberFormatException e) {
                Notification.show("Bad format of children number");
            }

            if (StringUtils.isBlank(name.getValue())) {
                containsContact = false;
                name.setComponentError(new UserError("Name needed"));
            }

            client.setChildrenComments(childrenComments.getValue());
            if (containsContact) {
                clientService.save(client);
                getUI().getNavigator().navigateTo(ThingsView.VIEW_NAME + "/message: Client saved");
            } else {
                email.setComponentError(new UserError("Add email"));
                vkLink.setComponentError(new UserError("Add VK link"));
                phone.setComponentError(new UserError("Bad format of phone"));
                phoneSecond.setComponentError(new UserError("Bad format of second phone"));
                Notification.show("Add at least one contact info (phone, second phone, email, VK link)");
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
