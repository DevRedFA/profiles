package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.entity.Identifiable;
import com.isedykh.profiles.service.ThingService;
import com.isedykh.profiles.service.entity.Term;
import com.isedykh.profiles.service.entity.Thing;
import com.isedykh.profiles.service.entity.ThingType;
import com.isedykh.profiles.service.ThingTypeService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;

@RequiredArgsConstructor
@SpringView(name = ThingsView.VIEW_NAME)
public class ThingsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "things";
    private static final int PAGE_SIZE = 16;

    private HorizontalLayout searchPanel = new HorizontalLayout();
    private Button buttonSearch = new Button("Search");
    private DateField begin = new DateField("Begin");
    private DateField stop = new DateField("End");
    private ComboBox<ThingType> type = new ComboBox<>("Type");

    private final ThingService thingService;

    private final ThingTypeService thingTypeService;

    @PostConstruct
    public void init() {

        begin.setDateFormat(Utils.DATE_FORMAT);

        stop.setDateFormat(Utils.DATE_FORMAT);

        Utils.setFieldIfNotNull(thingTypeService::findAll, type::setItems, s -> s);

        searchPanel.addComponent(type);
        searchPanel.addComponent(begin);
        searchPanel.addComponent(stop);
        searchPanel.addComponent(buttonSearch);
        searchPanel.setComponentAlignment(buttonSearch, Alignment.BOTTOM_LEFT);
        addComponent(searchPanel);

        AtomicReference<Page<Thing>> thingPage = new AtomicReference<>(thingService.findAll(PageRequest.of(0, PAGE_SIZE)));
        Grid<Thing> thingsGrid = new Grid<>();

        buttonSearch.addClickListener(clickEvent -> {
            // FIXME: 5/20/18 add event with null or empty choise of type
            List<Thing> allThingsByTypeFreeBetween = thingService.getAllThingsByTypeFreeBetween(type.getSelectedItem().get(), begin.getValue(), stop.getValue());
            thingsGrid.setItems(allThingsByTypeFreeBetween);

        });

        thingsGrid.setSizeFull();
        thingsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        thingsGrid.setItems(thingPage.get().getContent());
        thingsGrid.addColumn(Thing::getName).setCaption("Name");
        thingsGrid.addColumn(Thing::getType).setCaption("Type");
        thingsGrid.addColumn(Thing::getDeposit).setCaption("Deposit");
        thingsGrid.addColumn(Thing::getStatus).setCaption("Status");
        thingsGrid.addColumn(s ->
                s.getPrices()
                        .stream()
                        .filter(price -> price.getTerm().equals(new Term("week"))).findFirst().get())
                .setCaption("Week");
        thingsGrid.addColumn(s ->
                s.getPrices()
                        .stream()
                        .filter(price -> price.getTerm().equals(new Term("two weeks"))).findFirst().get())
                .setCaption("two weeks");
        thingsGrid.addColumn(s ->
                s.getPrices()
                        .stream()
                        .filter(price -> price.getTerm().equals(new Term("month"))).findFirst().get())
                .setCaption("month");
        thingsGrid.setHeightByRows(PAGE_SIZE);
        addComponent(thingsGrid);
        setExpandRatio(thingsGrid, 1f);


        buttonSearch.addClickListener(clickEvent -> {
            List<Thing> allThingsByTypeFreeBetween = thingService.getAllThingsByTypeFreeBetween(type.getSelectedItem().get(), begin.getValue(), stop.getValue());
            thingsGrid.setItems(allThingsByTypeFreeBetween);

        });

        thingsGrid.addItemClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clickEvent, this::getUI, ThingView.VIEW_NAME));

        HorizontalLayout buttons = new HorizontalLayout();
        HorizontalLayout leftButtons = new HorizontalLayout();
        Button buttonNext = new Button("Next");
        Button buttonPrevious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNewThing = new Button("New thing");
        Button buttonNewOrder = new Button("New order");
        Button buttonDelete = new Button("Delete");
        leftButtons.addComponent(buttonPrevious);
        leftButtons.addComponent(buttonDetails);
        leftButtons.addComponent(buttonNewThing);
        leftButtons.addComponent(buttonNewOrder);
        leftButtons.addComponent(buttonNext);
        buttons.addComponent(leftButtons);
        buttons.addComponent(buttonDelete);
        buttons.setSizeFull();
        buttons.setComponentAlignment(buttonDelete, Alignment.MIDDLE_RIGHT);
        buttonPrevious.setEnabled(false);

        buttonNext.addClickListener(getPageChangeClickListener(thingPage, Slice::nextPageable, thingsGrid, buttonNext, buttonPrevious, thingService));

        buttonPrevious.addClickListener(getPageChangeClickListener(thingPage, Slice::previousPageable, thingsGrid, buttonNext, buttonPrevious, thingService));

        buttonNewThing.addClickListener(clickEvent -> Utils.getNewClickListenerSupplier(this::getUI, ThingView.VIEW_NAME));

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
                Identifiable identifiable = Identifiable.class.cast(selectedItems.toArray()[0]);
                thingService.delete(identifiable.getId());
                getUI().getPage().reload();
            }
        });

        addComponent(buttons);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null && !event.getParameters().isEmpty()) {
            Notification.show(event.getParameters());
        }
    }
}
