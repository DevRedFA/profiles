package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.ClientService;
import com.isedykh.profiles.service.entity.Client;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
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

import static com.isedykh.profiles.common.Utils.PAGE_SIZE;
import static com.isedykh.profiles.common.Utils.getDeleteClickListener;
import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;
import static com.isedykh.profiles.view.ViewUtils.getButtonsLayout;
import static com.isedykh.profiles.view.ViewUtils.getClientGridWithSettings;

@Secured({"ROLE_ADMIN"})
@RequiredArgsConstructor
@SpringView(name = ClientsView.VIEW_NAME)
@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
public class ClientsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "clients";

    private final ClientService clientService;

    @PostConstruct
    public void init() {

        AtomicReference<Page<Client>> clientPage = new AtomicReference<>(clientService.findAll(PageRequest.of(0, PAGE_SIZE)));
        Grid<Client> clientGrid = getClientGridWithSettings();
        clientGrid.setItems(clientPage.get().getContent());
        clientGrid.addItemClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clickEvent, this::getUI, ClientView.VIEW_NAME));

        Button buttonSearch = new Button("Search");

        Button buttonNext = new Button("Next");
        Button buttonPrevious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNew = new Button("New");
        Button buttonDelete = new Button("Delete");

        ComboBox<String> nameFilter = new ComboBox<>("Name");
        nameFilter.setPageLength(20);
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

        buttonNext.addClickListener(getPageChangeClickListener(clientPage, Slice::nextPageable, clientGrid,
                buttonNext, buttonPrevious, clientService, nameFilter, Client::getName));

        buttonPrevious.addClickListener(getPageChangeClickListener(clientPage, Slice::previousPageable, clientGrid,
                buttonNext, buttonPrevious, clientService, nameFilter, Client::getName));

        buttonNew.addClickListener(clickEvent -> Utils.getNewClickListenerSupplier(this::getUI, ClientView.VIEW_NAME));

        buttonDetails.addClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clientGrid, this::getUI, ClientView.VIEW_NAME));

        buttonDelete.addClickListener(getDeleteClickListener(clientGrid, this::getUI, clientService));

        List<Client> content = clientPage.get().getContent();
        nameFilter.setItems(content.stream()
                .map(Client::getName)
                .collect(Collectors.toList()));

        TextField nameField = new TextField("Name");
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

        HorizontalLayout searchPanel = new HorizontalLayout();
        searchPanel.addComponent(nameFilter);
        searchPanel.addComponent(nameField);
        searchPanel.addComponent(buttonSearch);
        searchPanel.setComponentAlignment(buttonSearch, Alignment.BOTTOM_LEFT);

        HorizontalLayout buttons = getButtonsLayout(buttonDelete, buttonPrevious, buttonDetails, buttonNew, buttonNext);

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
