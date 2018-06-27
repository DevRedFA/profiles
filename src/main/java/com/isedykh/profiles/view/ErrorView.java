package com.isedykh.profiles.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

@UIScope
@SpringView
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class ErrorView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // do nothing
    }

    @PostConstruct
    void init() {
        Label label = new Label("Hello, this is the 'error view' loaded if no view is matched based on URL.");
        label.addStyleName(ValoTheme.LABEL_FAILURE);
        addComponent(label);
    }
}
