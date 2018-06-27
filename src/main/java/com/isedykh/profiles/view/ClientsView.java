package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.ClientService;
import com.isedykh.profiles.service.entity.Client;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.isedykh.profiles.common.Utils.*;

@Secured({"ROLE_ADMIN"})
@RequiredArgsConstructor
@SpringView(name = ClientsView.VIEW_NAME)
@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S2160"})
public class ClientsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "clients";

    private final ClientService clientService;

    private Grid<Client> clientGrid = new Grid<>();

    @PostConstruct
    public void init() {

        AtomicReference<Page<Client>> clientPage = new AtomicReference<>(clientService.findAll(PageRequest.of(0, PAGE_SIZE)));

        clientGrid.setSizeFull();
        clientGrid.setItems(clientPage.get().getContent());
        clientGrid.addColumn(Client::getName).setCaption("Name");
        clientGrid.addColumn(Client::getPhone).setCaption("Phone");
        clientGrid.addColumn(Client::getPhoneSecond).setCaption("Second phone");
        clientGrid.addColumn(Client::getEmail).setCaption("Email");
        clientGrid.addColumn(Client::getVkLink).setCaption("VK link");
        clientGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        clientGrid.setHeightByRows(PAGE_SIZE);

        HorizontalLayout searchPanel = new HorizontalLayout();


        ComboBox<String> nameFilter = new ComboBox<>("Name");
        nameFilter.setPageLength(20);

        TextField nameField = new TextField("Name");

        Button buttonSearch = new Button("Search");
        searchPanel.addComponent(nameFilter);
        searchPanel.addComponent(nameField);
        searchPanel.addComponent(buttonSearch);
        searchPanel.setComponentAlignment(buttonSearch, Alignment.BOTTOM_LEFT);

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

        nameFilter.addValueChangeListener(event -> {
            String name = event.getValue();
            List<Client> clientByName = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                clientByName.addAll(clientService.findClientByName(name));

            } else {
                Page<Client> all = clientService.findAll(PageRequest.of(0, PAGE_SIZE));
                clientByName.addAll(all.getContent());
                buttonNext.setEnabled(!all.isLast());
                buttonPrevious.setEnabled(!all.isFirst());
            }
            clientGrid.setItems(clientByName);
        });

        List<Client> content = clientPage.get().getContent();
        nameFilter.setItems(content.stream()
                .map(Client::getName)
                .collect(Collectors.toList()));
        clientGrid.addItemClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clickEvent, this::getUI, ClientView.VIEW_NAME));

        buttonSearch.addClickListener(event -> {
            String value = nameField.getValue();
            List<Client> clientByName = new ArrayList<>();
            if (!value.isEmpty()) {
                clientByName.addAll(clientService.findClientByName(value));
            } else {
                Page<Client> all = clientService.findAll(PageRequest.of(0, PAGE_SIZE));
                clientByName.addAll(all.getContent());
                buttonNext.setEnabled(!all.isLast());
                buttonPrevious.setEnabled(!all.isFirst());
            }
            clientGrid.setItems(clientByName);
            nameFilter.setItems(clientByName.stream().map(Client::getName));
        });

        buttonNext.addClickListener(getPageChangeClickListener(clientPage, Slice::nextPageable, clientGrid,
                buttonNext, buttonPrevious, clientService, nameFilter, Client::getName));

        buttonPrevious.addClickListener(getPageChangeClickListener(clientPage, Slice::previousPageable, clientGrid,
                buttonNext, buttonPrevious, clientService, nameFilter, Client::getName));

        buttonNew.addClickListener(clickEvent -> Utils.getNewClickListenerSupplier(this::getUI, ClientView.VIEW_NAME));

        buttonDetails.addClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clientGrid, this::getUI, ClientView.VIEW_NAME));

        buttonDelete.addClickListener(getDeleteClickListener(clientGrid, this::getUI, clientService));

        addComponent(searchPanel);

        addComponent(clientGrid);

        addComponent(buttons);

        setExpandRatio(clientGrid, 1f);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // do nothing
    }
}
