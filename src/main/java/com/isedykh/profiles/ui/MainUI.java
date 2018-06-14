package com.isedykh.profiles.ui;

import com.isedykh.profiles.view.*;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import org.vaadin.spring.security.VaadinSecurity;

import javax.annotation.PostConstruct;


@Theme("Demo")
@SpringUI(path = "/")
@SpringViewDisplay
@RequiredArgsConstructor
public class MainUI extends UI implements ViewDisplay {

    private final SpringNavigator springNavigator;

    private final SpringViewProvider springViewProvider;

    private final VaadinSecurity vaadinSecurity;

    private Panel springViewDisplay;

    @Override
    public void showView(View view) {
        springViewDisplay.setContent((Component) view);
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Procaton storage client");
        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton("Things", ThingsView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Clients", ClientsView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Orders", OrdersView.VIEW_NAME));
        navigationBar.addComponent(new Button("Logout", e -> vaadinSecurity.logout()));
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.addComponent(navigationBar);
        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);
        setContent(root);
    }

    @PostConstruct
    public void init() {
        springNavigator.setErrorView(ErrorView.class);
        springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }
}
