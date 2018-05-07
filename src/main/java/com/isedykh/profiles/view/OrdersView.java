package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.mapper.OrderMapper;
import com.isedykh.profiles.service.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;

@AllArgsConstructor
@SpringView(name = OrdersView.VIEW_NAME)
public class OrdersView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "orders";

    private OrderService orderService;

    @PostConstruct
    public void init() {

        AtomicReference<Page<Order>> orderPage = new AtomicReference<>(orderService.findAll(PageRequest.of(0, 17)));
        Grid<Order> orderGrid = new Grid<>();
        orderGrid.setSizeFull();
        orderGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        orderGrid.setItems(orderPage.get().getContent());
        orderGrid.addColumn(Order::getId).setCaption("Id");
        orderGrid.addColumn(s -> s.getThing().getName()).setCaption("Thing");
        orderGrid.addColumn(s -> s.getClient().getName()).setCaption("Client");
        orderGrid.addColumn(Order::getPrice).setCaption("Price");
        orderGrid.addColumn(Order::getStatus).setCaption("Status");
        orderGrid.addColumn(Order::getBegin).setCaption("Begin");
        orderGrid.addColumn(Order::getEnd).setCaption("End");
        orderGrid.addColumn(Order::getComments).setCaption("Comments");
        orderGrid.setHeightByRows(17);
        addComponent(orderGrid);
        setExpandRatio(orderGrid, 1f);

        orderGrid.addItemClickListener(clickEvent -> Utils.detailsDoubleClickListenerSupplier.accept(clickEvent, this::getUI));

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

        buttonNext.addClickListener(getPageChangeClickListener(orderPage, Slice::nextPageable, orderGrid, buttonNext, buttonPrevious, orderService));

        buttonPrevious.addClickListener(getPageChangeClickListener(orderPage, Slice::previousPageable, orderGrid, buttonNext, buttonPrevious, orderService));

        buttonNew.addClickListener(clickEvent -> Utils.newClickListenerSupplier.accept(this::getUI));

        buttonDetails.addClickListener(clickEvent -> Utils.detailsClickListenerSupplier.accept(orderGrid, this::getUI));

        addComponent(buttons);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
