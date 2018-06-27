package com.isedykh.profiles.view;

import com.isedykh.profiles.service.CrudService;
import com.isedykh.profiles.service.entity.Client;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S2160"})
class ClientTemplate extends Window {

    private TextField nameField = new TextField("Enter new name");
    private TextField phoneField = new TextField("Enter phone");

    ClientTemplate(CrudService<Client> crudService) {
        super("New Client");
        center();
        Button saveButton = new Button("Save", event -> {
            Client client = new Client();
            client.setName(nameField.getValue());
            client.setPhone(Long.valueOf(phoneField.getValue()));
            crudService.save(client);
            getUI().getPage().reload();
            close();
        });
        VerticalLayout layout = new VerticalLayout();
        layout.addComponentsAndExpand(nameField, phoneField,
                saveButton);
        layout.setHeight(14f, Unit.EM);
        setClosable(true);

        setResizable(true);
        setContent(layout);
    }
}
