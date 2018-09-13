package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.OrderService;
import com.isedykh.profiles.service.ThingService;
import com.isedykh.profiles.service.ThingTypeService;
import com.isedykh.profiles.service.entity.Identifiable;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.entity.Thing;
import com.isedykh.profiles.service.entity.ThingType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.PAGE_SIZE;
import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;
import static com.isedykh.profiles.view.ViewUtils.getButtonsLayout;

@RequiredArgsConstructor
@SpringView(name = ThingsView.VIEW_NAME)
@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
public class ThingsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "things";

    private final ThingService thingService;
    private final ThingTypeService thingTypeService;
    private final OrderService orderService;

    @PostConstruct
    public void init() {

        Grid<Thing> thingsGrid = new Grid<>();
        thingsGrid.setSizeFull();
        thingsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        thingsGrid.addColumn(Thing::getName).setCaption("Name");
        thingsGrid.addColumn(Thing::getType).setCaption("Type");
        thingsGrid.addColumn(thing -> thing.getPurchasePrice()/100).setCaption("Purchase Price");
        thingsGrid.addColumn(thing -> thingService.countAllActualPrices(thing)/100).setCaption("Earned");
        thingsGrid.setHeightByRows(PAGE_SIZE);
        thingsGrid.addItemClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clickEvent, this::getUI, ThingView.VIEW_NAME));

        AtomicReference<Page<Thing>> thingPage = new AtomicReference<>(thingService.findAll(PageRequest.of(0, PAGE_SIZE)));
        thingsGrid.setItems(thingPage.get().getContent());

        Button buttonNext = new Button("Next");
        Button buttonPrevious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNewThing = new Button("New thing");
        Button buttonNewOrder = new Button("New order");
        Button buttonDelete = new Button("Delete");

        buttonNext.addClickListener(getPageChangeClickListener(thingPage, Slice::nextPageable, thingsGrid, buttonNext, buttonPrevious, thingService));

        buttonPrevious.addClickListener(getPageChangeClickListener(thingPage, Slice::previousPageable, thingsGrid, buttonNext, buttonPrevious, thingService));

        buttonNewThing.addClickListener(clickEvent -> Utils.getNewClickListenerSupplier(this::getUI, ThingView.VIEW_NAME));

        buttonNext.setEnabled(thingPage.get().hasNext());

        buttonPrevious.setEnabled(thingPage.get().hasPrevious());

        DateField begin = new DateField("Begin");
        begin.setDateFormat(Utils.DATE_FORMAT);

        DateField stop = new DateField("End");
        stop.setDateFormat(Utils.DATE_FORMAT);

        buttonNewOrder.addClickListener(clickEvent -> {
            StringBuilder s = new StringBuilder(OrderView.VIEW_NAME + "/new");
            if (thingsGrid.getSelectedItems().size() == 1) {
                s.append("/thing=").append(thingsGrid.getSelectedItems().toArray(new Thing[0])[0].getId());
            }
            if (begin.getValue() != null) {
                s.append("/begin=").append(begin.getValue());
            }
            if (stop.getValue() != null) {
                s.append("/stop=").append(stop.getValue());
            }
            getUI().getNavigator().navigateTo(s.toString());
        });

        buttonDetails.addClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(thingsGrid, this::getUI, ThingView.VIEW_NAME));

        buttonDelete.addClickListener(clickEvent -> {
            Set selectedItems = thingsGrid.getSelectedItems();
            if (selectedItems.size() == 1) {
                Identifiable identifiable = (Identifiable) selectedItems.toArray()[0];
                List<Order> thingOrderHistory = orderService.getThingOrderHistory(identifiable.getId());
                orderService.delete(thingOrderHistory);
                thingService.delete(identifiable.getId());
                getUI().getPage().reload();
            }
        });

        ComboBox<ThingType> type = new ComboBox<>("Type");
        Utils.setFieldIfNotNull(thingTypeService::findAll, type::setItems, s -> s);

        Button buttonSearch = new Button("Search");
        buttonSearch.addClickListener(clickEvent -> {
            List<Thing> allThingsByTypeFreeBetween = thingService.getAllThingsByTypeFreeBetween(type.getSelectedItem().get(), begin.getValue(), stop.getValue());
            thingsGrid.setItems(allThingsByTypeFreeBetween);
        });

        HorizontalLayout searchPanel = new HorizontalLayout();
        searchPanel.addComponent(type);
        searchPanel.addComponent(begin);
        searchPanel.addComponent(stop);
        searchPanel.addComponent(buttonSearch);
        searchPanel.setComponentAlignment(buttonSearch, Alignment.BOTTOM_LEFT);

        HorizontalLayout buttons = getButtonsLayout(buttonDelete, buttonPrevious, buttonDetails, buttonNewThing, buttonNewOrder, buttonNext);

        addComponent(searchPanel);
        addComponent(thingsGrid);
        addComponent(buttons);
        setExpandRatio(thingsGrid, 1f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // do nothing
    }
}
