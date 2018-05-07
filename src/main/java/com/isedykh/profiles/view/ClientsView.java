package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.ClientService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.detailsClickListenerSupplier;
import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;

@AllArgsConstructor
@SpringView(name = ClientsView.VIEW_NAME)
public class ClientsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "clients";

    private ClientService clientService;

    @PostConstruct
    public void init() {
        AtomicReference<Page<Client>> clientPage = new AtomicReference<>(clientService.findAll(PageRequest.of(0, 17)));
        Grid<Client> clientGrid = new Grid<>();
        clientGrid.setSizeFull();
        clientGrid.setItems(clientPage.get().getContent());
        clientGrid.addColumn(Client::getName).setCaption("Name");
        clientGrid.addColumn(Client::getPhone).setCaption("Phone");
        clientGrid.addColumn(Client::getPhoneSecond).setCaption("Second phone");
        clientGrid.addColumn(Client::getEmail).setCaption("Email");
        clientGrid.addColumn(Client::getContactLink).setCaption("Contact link");
        clientGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        clientGrid.setHeightByRows(17);
        addComponent(clientGrid);
        setExpandRatio(clientGrid, 1f);

        clientGrid.addItemClickListener(clickEvent -> Utils.detailsDoubleClickListenerSupplier.accept(clickEvent, this::getUI));

        HorizontalLayout buttons = new HorizontalLayout();
        Button buttonNext = new Button("Next");
        Button buttonPrevious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNew = new Button("New");

        buttons.addComponent(buttonPrevious);
        buttons.addComponent(buttonDetails);
        buttons.addComponent(buttonNew);
        buttons.addComponent(buttonNext);

        buttonPrevious.setEnabled(false);

        buttonNext.addClickListener(getPageChangeClickListener(clientPage, Slice::nextPageable, clientGrid, buttonNext, buttonPrevious, clientService));

        buttonPrevious.addClickListener(getPageChangeClickListener(clientPage, Slice::previousPageable, clientGrid, buttonNext, buttonPrevious, clientService));

        buttonNew.addClickListener(clickEvent -> Utils.newClickListenerSupplier.accept(this::getUI));

        buttonDetails.addClickListener(clickEvent -> detailsClickListenerSupplier.accept(clientGrid, this::getUI));

        addComponent(buttons);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {


    }


}
