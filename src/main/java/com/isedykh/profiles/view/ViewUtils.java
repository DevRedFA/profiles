package com.isedykh.profiles.view;

import com.isedykh.profiles.service.entity.Client;
import com.isedykh.profiles.service.entity.Order;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import lombok.experimental.UtilityClass;

import static com.isedykh.profiles.common.Utils.PAGE_SIZE;

@UtilityClass
class ViewUtils {

    static HorizontalLayout getButtonsLayout(Button buttonDelete, Button... buttons) {
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        HorizontalLayout leftButtons = new HorizontalLayout();
        leftButtons.addComponents(buttons);
        buttonsLayout.addComponent(leftButtons);
        buttonsLayout.addComponent(buttonDelete);
        buttonsLayout.setSizeFull();
        buttonsLayout.setComponentAlignment(buttonDelete, Alignment.MIDDLE_RIGHT);
        buttons[0].setEnabled(false);
        return buttonsLayout;
    }


    static Grid<Order> getOrderGridWithSettings() {
        Grid<Order> orderGrid = new Grid<>();
        orderGrid.setSizeFull();
        orderGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        orderGrid.addColumn(Order::getId).setCaption("Id");
        orderGrid.addColumn(s -> s.getThing().getName()).setCaption("Thing");
        orderGrid.addColumn(Order::getPrice).setCaption("Price");
        orderGrid.addColumn(order -> order.getActualPrice() / 100).setCaption("Actual price");
        orderGrid.addColumn(Order::getStatus).setCaption("Status");
        orderGrid.addColumn(Order::getBegin).setCaption("Begin");
        orderGrid.addColumn(Order::getStop).setCaption("End");
        orderGrid.addColumn(Order::getComments).setCaption("Comments");
        orderGrid.setHeightByRows(PAGE_SIZE);
        return orderGrid;
    }

    static Grid<Client> getClientGridWithSettings() {
        Grid<Client> clientGrid = new Grid<>();
        clientGrid.setSizeFull();
        clientGrid.addColumn(Client::getName).setCaption("Name");
        clientGrid.addColumn(Client::getPhone).setCaption("Phone");
        clientGrid.addColumn(Client::getPhoneSecond).setCaption("Second phone");
        clientGrid.addColumn(Client::getEmail).setCaption("Email");
        clientGrid.addColumn(Client::getVkLink).setCaption("VK link");
        clientGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        clientGrid.setHeightByRows(PAGE_SIZE);
        return clientGrid;
    }
}
