package com.isedykh.profiles.view;

import com.isedykh.profiles.service.CrudService;
import com.isedykh.profiles.service.entity.Nameable;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
class WindowTemplate<T extends Nameable> extends Window {


    WindowTemplate(Class<T> clazz, CrudService<T> crudService) {
        super("Name:");
        center();
        TextField nameField = new TextField("Enter new name");
        Button saveButton = new Button("Save", event -> {
            try {
                T t = clazz.newInstance();
                t.setName(nameField.getValue());
                crudService.save(t);
                getUI().getPage().reload();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            close();
        });
        VerticalLayout layout = new VerticalLayout();
        layout.addComponentsAndExpand(nameField,
                saveButton);
        layout.setHeight(10f, Unit.EM);
        setClosable(true);
        setResizable(true);
        setContent(layout);
    }
}
