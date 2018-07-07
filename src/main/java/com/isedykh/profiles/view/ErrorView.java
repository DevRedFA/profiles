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
@SuppressWarnings({"squid:S1948","squid:MaximumInheritanceDepth","squid:S2160"})
public class ErrorView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // do nothing
    }

    @PostConstruct
    void init() {
        Label label = new Label("Ooops, something went wrong. Sorry!");
        label.addStyleName(ValoTheme.LABEL_FAILURE);
        addComponent(label);
    }
}
