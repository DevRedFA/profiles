package com.isedykh.profiles.view;

import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.initTailMenu;

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
        clientGrid.addItemClickListener(clickEvent -> {
            if (clickEvent.getMouseEventDetails().isDoubleClick()) {
                Client client = clickEvent.getItem();
                String s = ClientView.VIEW_NAME + "/" + client.getId();
                getUI().getNavigator().navigateTo(s);
            }
        });

//        addComponent(initTailMenu(clientGrid,getUI()));

        HorizontalLayout buttons = new HorizontalLayout();
        Button buttonNext = new Button("Next");
        Button buttonPrivious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNew = new Button("New");

        buttons.addComponent(buttonPrivious);
        buttons.addComponent(buttonDetails);
        buttons.addComponent(buttonNew);
        buttons.addComponent(buttonNext);

        buttonPrivious.setEnabled(false);


        buttonNext.addClickListener(clickEvent -> {
            clientPage.set(clientService.findAll(clientPage.get().nextPageable()));
            clientGrid.setItems(clientPage.get().getContent());
            buttonNext.setEnabled(!clientPage.get().isLast());
            buttonPrivious.setEnabled(true);
        });

        buttonPrivious.addClickListener(clickEvent -> {
            clientPage.set(clientService.findAll(clientPage.get().previousPageable()));
            clientGrid.setItems(clientPage.get().getContent());
            buttonPrivious.setEnabled(!clientPage.get().isFirst());
            buttonNext.setEnabled(true);
        });

        buttonNew.addClickListener(clickEvent -> {
            String state = getUI().getNavigator().getState();
            String s = state.substring(0, state.length() - 1) + "/new";
            getUI().getNavigator().navigateTo(s);
        });

        buttonDetails.addClickListener(clickEvent -> {
            Set selectedItems = clientGrid.getSelectedItems();
            if (selectedItems.size() == 1) {
                Object[] objects = selectedItems.toArray();
                Identifiable obj = Identifiable.class.cast(objects[0]);
                String state = getUI().getNavigator().getState();
                String s = state.substring(0, state.length() - 1) + "/" + obj.getId();
                getUI().getNavigator().navigateTo(s);
            } else {
                Notification.show("Please select one option");
            }
        });

        addComponent(buttons);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {


    }


}
