package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.ClientService;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.OrderService;
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

import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;

@AllArgsConstructor
@SpringView(name = OrdersView.VIEW_NAME)
public class OrdersView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "orders";
    public static final int PAGE_SIZE = 17;

    private OrderService orderService;

    private ClientService clientService;

    @PostConstruct
    public void init() {

        AtomicReference<Page<Order>> orderPage = new AtomicReference<>(orderService.findAll(PageRequest.of(0, PAGE_SIZE)));
        Grid<Order> orderGrid = new Grid<>();
        orderGrid.setSizeFull();
        orderGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        orderGrid.setItems(orderPage.get().getContent());
        orderGrid.addColumn(Order::getId).setCaption("Id");
        orderGrid.addColumn(s -> s.getThing().getName()).setCaption("Thing");
        orderGrid.addColumn(Order::getPrice).setCaption("Price");
        orderGrid.addColumn(Order::getStatus).setCaption("Status");
        orderGrid.addColumn(Order::getBegin).setCaption("Begin");
        orderGrid.addColumn(Order::getStop).setCaption("End");
        orderGrid.addColumn(Order::getComments).setCaption("Comments");
        orderGrid.setHeightByRows(PAGE_SIZE);
        addComponent(orderGrid);
        setExpandRatio(orderGrid, 1f);

        orderGrid.addItemClickListener(clickEvent -> Utils.detailsDoubleClickListenerSupplier.accept(clickEvent, this::getUI));

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

        buttonNext.addClickListener(getPageChangeClickListener(orderPage, Slice::nextPageable, orderGrid, buttonNext, buttonPrevious, orderService));

        buttonPrevious.addClickListener(getPageChangeClickListener(orderPage, Slice::previousPageable, orderGrid, buttonNext, buttonPrevious, orderService));

        buttonNew.addClickListener(clickEvent -> Utils.newClickListenerSupplier.accept(this::getUI));

        buttonDetails.addClickListener(clickEvent -> Utils.detailsClickListenerSupplier.accept(orderGrid, this::getUI));

        addComponent(buttons);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null && !event.getParameters().isEmpty()) {
            Notification.show(event.getParameters());
        }
    }
}
