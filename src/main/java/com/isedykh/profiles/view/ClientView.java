package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.*;
import com.isedykh.profiles.service.entity.Client;
import com.isedykh.profiles.service.entity.Identifiable;
import com.isedykh.profiles.service.entity.Order;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.FileResource;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static com.isedykh.profiles.common.Utils.START_POINT;

@RequiredArgsConstructor
@SpringView(name = ClientView.VIEW_NAME)
@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S2160"})
public class ClientView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "client";

    private final ClientService clientService;

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

    private VerticalLayout paramLayout = new VerticalLayout();

    private HorizontalLayout mainLayout = new HorizontalLayout();

    private VerticalLayout gridsLayout = new VerticalLayout();

    private Grid<Order> ordersGrid = new Grid<>();

    private Grid<String> picturesGrid = new Grid<>();

    private HorizontalLayout picturesLayout = new HorizontalLayout();

    private ImageReceiver receiver = new ImageReceiver();

    private final Upload upload = new Upload("Upload it here", receiver);

    @PostConstruct
    public void init() {

        paramLayout.addComponent(name);
        paramLayout.addComponent(phone);
        paramLayout.addComponent(phoneSecond);
        paramLayout.addComponent(email);
        paramLayout.addComponent(vkLink);
        paramLayout.addComponent(address);
        paramLayout.addComponent(childrenNumber);
        paramLayout.addComponent(childrenComments);
        paramLayout.addComponent(save);

        name.setWidth("100%");
        phone.setWidth("100%");
        phoneSecond.setWidth("100%");
        email.setWidth("100%");
        vkLink.setWidth("100%");
        address.setWidth("100%");
        childrenNumber.setWidth("100%");
        childrenComments.setWidth("100%");
        paramLayout.setWidth("100%");
        mainLayout.addComponent(paramLayout);
        mainLayout.addComponent(gridsLayout);
        mainLayout.setExpandRatio(paramLayout, 1f);
        mainLayout.setExpandRatio(gridsLayout, 3f);
        gridsLayout.addComponent(ordersGrid);
        gridsLayout.addComponent(picturesLayout);
        picturesLayout.addComponent(picturesGrid);
        picturesLayout.addComponent(upload);
        mainLayout.setWidth("100%");
        addComponent(mainLayout);

        picturesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        picturesGrid.setHeightByRows(4);
        ordersGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        ordersGrid.setSizeFull();
        setExpandRatio(mainLayout, 1f);


        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);


    }

    class ImageReceiver implements Upload.Receiver, Upload.SucceededListener {
        private static final long serialVersionUID = -1276759102490466761L;

        private File file;

        public OutputStream receiveUpload(String filename,
                                          String mimeType) {
            FileOutputStream fos;
            try {
                file = new File(START_POINT + client.getName().replace(" ", "_") + "_" + client.getPathsToPhoto().size());
                fos = new FileOutputStream(file);
            } catch (final java.io.FileNotFoundException e) {
                new Notification("Could not open file",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return null;
            }
            return fos;
        }

        public void uploadSucceeded(Upload.SucceededEvent event) {
            client.getPathsToPhoto().add(file.getAbsolutePath());
            clientService.save(client);
            getUI().getPage().reload();
        }
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

                    Utils.setFieldIfNotNull(client::getPathsToPhoto, picturesGrid::setItems, s -> s);

                } catch (Exception e) {
                    Notification.show("Client with such id not found");
                }
            }
        }


        picturesGrid.addColumn(s -> s.substring(s.lastIndexOf('/'))).setCaption("File name");

        picturesGrid.addItemClickListener(clickEvent -> {
            if (clickEvent.getMouseEventDetails().isDoubleClick()) {
                String item = clickEvent.getItem();
                final Image image = new Image();
                image.setVisible(true);
                File file = new File(item);
                image.setSource(new FileResource(file));
                Window components = new Window(clickEvent.getItem()) {

                };
                components.setContent(image);
                UI.getCurrent().addWindow(components);
            }
        });


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
                getUI().getNavigator().navigateTo(ClientsView.VIEW_NAME + "/message: Client saved");
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
