package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.ClientService;
import com.isedykh.profiles.service.OrderService;
import com.isedykh.profiles.service.entity.Client;
import com.isedykh.profiles.service.entity.Order;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import static com.isedykh.profiles.common.Utils.START_POINT;
import static com.isedykh.profiles.view.ViewUtils.getOrderGridWithSettings;

@RequiredArgsConstructor
@SpringView(name = ClientView.VIEW_NAME)
@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
public class ClientView extends VerticalLayout implements View {

    static final String VIEW_NAME = "client";

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

    private ImageReceiver receiver = new ImageReceiver();
    private final Upload upload = new Upload("Upload it here", receiver);

    private Grid<Order> ordersGrid = getOrderGridWithSettings();

    private Grid<String> picturesGrid = new Grid<>();

    @PostConstruct
    public void init() {

        ordersGrid.addItemClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clickEvent, this::getUI, OrderView.VIEW_NAME));
        ordersGrid.setHeightByRows(10);

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

        Button save = new Button("Save");
        save.addClickListener(clickEvent -> {
            boolean containsContact = false;

            client.setName(name.getValue());

            if (!StringUtils.isBlank(phoneSecond.getValue())) {
                client.setEmail(email.getValue());

            }

            if (!StringUtils.isBlank(vkLink.getValue())) {
                client.setVkLink(vkLink.getValue());
                containsContact = true;
            }

            if (!StringUtils.isBlank(address.getValue())) {
                client.setAddress(address.getValue());
            }

            setChildrenNumberIfValid();

            if (StringUtils.isBlank(name.getValue())) {
                containsContact = false;
                name.setComponentError(new UserError("Name needed"));
            }

            containsContact = isPhoneValid(containsContact, phone, client::setPhone);

            containsContact = isPhoneValid(containsContact, phoneSecond, client::setPhoneSecond);

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

        name.setWidth("100%");
        phone.setWidth("100%");
        phoneSecond.setWidth("100%");
        email.setWidth("100%");
        vkLink.setWidth("100%");
        address.setWidth("100%");
        childrenNumber.setWidth("100%");
        childrenComments.setWidth("100%");

        picturesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        picturesGrid.setHeightByRows(4);

        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);

        HorizontalLayout picturesLayout = new HorizontalLayout();
        picturesLayout.addComponent(picturesGrid);
        picturesLayout.addComponent(upload);

        VerticalLayout gridsLayout = new VerticalLayout();
        gridsLayout.addComponent(ordersGrid);
        gridsLayout.addComponent(picturesLayout);

        VerticalLayout paramLayout = new VerticalLayout();
        paramLayout.addComponent(name);
        paramLayout.addComponent(phone);
        paramLayout.addComponent(phoneSecond);
        paramLayout.addComponent(email);
        paramLayout.addComponent(vkLink);
        paramLayout.addComponent(address);
        paramLayout.addComponent(childrenNumber);
        paramLayout.addComponent(childrenComments);
        paramLayout.addComponent(save);
        paramLayout.setWidth("100%");

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.addComponent(paramLayout);
        mainLayout.addComponent(gridsLayout);
        mainLayout.setExpandRatio(paramLayout, 1f);
        mainLayout.setExpandRatio(gridsLayout, 3f);
        mainLayout.setWidth("100%");

        addComponent(mainLayout);
        setExpandRatio(mainLayout, 1f);
    }

    private void setChildrenNumberIfValid() {
        try {
            if (!StringUtils.isBlank(childrenNumber.getValue())) {
                client.setChildrenNumber(Integer.parseInt(childrenNumber.getValue()));
            }
        } catch (NumberFormatException e) {
            Notification.show("Bad format of children number");
        }
    }

    private boolean isPhoneValid(boolean containsContact, TextField phone, Consumer<Long> clientField) {
        try {
            String phoneNumber = phone.getValue();
            if (!StringUtils.isBlank(phoneNumber)) {
                clientField.accept(Long.parseLong(phoneNumber));
                containsContact = true;
                phone.setComponentError(null);
            }
        } catch (NumberFormatException e) {
            phone.setComponentError(new UserError("Bad format of phone"));
        }
        return containsContact;
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
    }
}
