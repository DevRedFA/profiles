package com.isedykh.profiles.view;

import com.isedykh.profiles.service.CrudService;
import com.isedykh.profiles.service.entity.Nameable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindowTemplate<T extends Nameable> extends Window {

    private TextField nameField = new TextField("Enter new name");
    private VerticalLayout layout = new VerticalLayout();

    public WindowTemplate(Class<T> clazz, CrudService<T> crudService) {
        super("Name:");
        center();
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
        layout.addComponentsAndExpand(nameField,
                saveButton);
        layout.setHeight(10f,Unit.EM);
//        layout.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
        setClosable(true);
        setResizable(true);
        setContent(layout);
    }
}