package com.isedykh.profiles.view;

import com.isedykh.profiles.service.CrudService;
import com.isedykh.profiles.service.entity.Nameable;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class WindowTemplate<T extends Nameable> extends Window {

    private TextField nameField = new TextField("Enter new name");

    public WindowTemplate(Class<T> clazz, CrudService<T> crudService) {
        super("New type");
        center();

        setClosable(true);

        setContent(new Button("Close me", event -> {
            try {
                T t = clazz.newInstance();
                t.setName(nameField.getValue());
                crudService.save(t);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            close();
        }));
    }
}
