package com.isedykh.profiles.view;

import com.isedykh.profiles.service.CrudService;
import com.isedykh.profiles.service.entity.Term;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings({"squid:S1948","squid:MaximumInheritanceDepth","squid:S2160"})
class TermTemplate extends Window {

    TermTemplate(CrudService<Term> crudService) {
        super("Name:");
        center();

        TextField nameField = new TextField("Enter new name");
        TextField coefficientField = new TextField("Enter coefficient");
        Button saveButton = new Button("Save", event -> {
            Term term = new Term();
            term.setName(nameField.getValue());
            double coeffInDouble = Double.parseDouble(coefficientField.getValue());
            term.setCoefficient((int) (coeffInDouble * 100));
            crudService.save(term);
            getUI().getPage().reload();
            close();
        });
        VerticalLayout layout = new VerticalLayout();
        layout.addComponentsAndExpand(nameField, coefficientField,
                saveButton);
        layout.setHeight(14f,Unit.EM);
        setClosable(true);

        setResizable(true);
        setContent(layout);
    }
}
