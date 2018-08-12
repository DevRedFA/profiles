package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.OrderService;
import com.isedykh.profiles.service.entity.Identifiable;
import com.isedykh.profiles.service.entity.Order;
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
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.PAGE_SIZE;
import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;
import static com.isedykh.profiles.view.ViewUtils.getButtonsLayout;
import static com.isedykh.profiles.view.ViewUtils.getOrderGridWithSettings;

@AllArgsConstructor
@SpringView(name = OrdersView.VIEW_NAME)
@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
public class OrdersView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "orders";

    private OrderService orderService;

    @PostConstruct
    public void init() {

        AtomicReference<Page<Order>> orderPage = new AtomicReference<>(orderService.findAll(PageRequest.of(0, PAGE_SIZE, Sort.Direction.DESC, "id")));
        Grid<Order> orderGrid = getOrderGridWithSettings();

        orderGrid.setItems(orderPage.get().getContent());
        orderGrid.addItemClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clickEvent, this::getUI, OrderView.VIEW_NAME));

        addComponent(orderGrid);
        setExpandRatio(orderGrid, 1f);

        Button buttonNext = new Button("Next");
        Button buttonPrevious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNew = new Button("New");
        Button buttonClose = new Button("Close");
        Button buttonDelete = new Button("Delete");

        HorizontalLayout buttons = getButtonsLayout(buttonDelete, buttonPrevious, buttonDetails, buttonNew, buttonNext, buttonClose);

        buttonNext.addClickListener(getPageChangeClickListener(orderPage, Slice::nextPageable, orderGrid, buttonNext, buttonPrevious, orderService));

        buttonPrevious.addClickListener(getPageChangeClickListener(orderPage, Slice::previousPageable, orderGrid, buttonNext, buttonPrevious, orderService));

        buttonNew.addClickListener(clickEvent -> Utils.getNewClickListenerSupplier(this::getUI, OrderView.VIEW_NAME));

        buttonDetails.addClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(orderGrid, this::getUI, OrderView.VIEW_NAME));

        buttonClose.addClickListener(clickEvent -> {
            Set selectedItems = orderGrid.getSelectedItems();
            if (selectedItems.size() == 1) {
                Identifiable identifiable = (Identifiable) selectedItems.toArray()[0];
                orderService.closeOrder(identifiable.getId());
                getUI().getPage().reload();
            }
        });

        buttonDelete.addClickListener(clickEvent -> {
            Set selectedItems = orderGrid.getSelectedItems();
            if (selectedItems.size() == 1) {
                Identifiable identifiable = (Identifiable) selectedItems.toArray()[0];
                orderService.delete(identifiable.getId());
                getUI().getPage().reload();
            }
        });

        addComponent(buttons);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // do nothing
    }
}
