package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.ClientService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.getDeleteClickListener;
import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;
//@Secured({"ROLE_ADMIN"})
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
        HorizontalLayout leftButtons = new HorizontalLayout();
        Button buttonNext = new Button("Next");
        Button buttonPrevious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNew = new Button("New");
        Button buttonDelete = new Button("Delete");
        leftButtons.addComponent(buttonPrevious);
        leftButtons.addComponent(buttonDetails);
        leftButtons.addComponent(buttonNew);
        leftButtons.addComponent(buttonNext);
        buttons.addComponent(leftButtons);
        buttons.addComponent(buttonDelete);
        buttons.setSizeFull();
        buttons.setComponentAlignment(buttonDelete, Alignment.MIDDLE_RIGHT);
        buttonPrevious.setEnabled(false);

        // FIXME: 14.05.2018 broken next button
        buttonNext.addClickListener(getPageChangeClickListener(clientPage, Slice::nextPageable, clientGrid, buttonNext, buttonPrevious, clientService));

        buttonPrevious.addClickListener(getPageChangeClickListener(clientPage, Slice::previousPageable, clientGrid, buttonNext, buttonPrevious, clientService));

        buttonNew.addClickListener(clickEvent -> Utils.newClickListenerSupplier.accept(this::getUI));

        buttonDetails.addClickListener(clickEvent -> Utils.detailsClickListenerSupplier.accept(clientGrid, this::getUI));

        // TODO: 07.05.2018 add reset of grid 
        buttonDelete.addClickListener(getDeleteClickListener(clientGrid, clientService));

        addComponent(buttons);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
