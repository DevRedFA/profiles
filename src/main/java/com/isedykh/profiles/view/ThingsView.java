package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.dao.entity.ThingType;
import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.ThingDto;
import com.isedykh.profiles.service.ThingDtoService;
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
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;

@RequiredArgsConstructor
@SpringView(name = ThingsView.VIEW_NAME)
public class ThingsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "things";
    private static final int PAGE_SIZE = 16;


    private final ThingDtoService thingService;

    @PostConstruct
    public void init() {

        HorizontalLayout searchPanel = new HorizontalLayout();

        Button buttonSearch = new Button("Search");
        DateField begin = new DateField("Begin");
        DateField stop = new DateField("End");
        ComboBox<ThingType> type = new ComboBox<>("Type");
        Utils.setFieldIfNotNull(ThingType::values, type::setItems, s -> s);

        searchPanel.addComponent(type);
        searchPanel.addComponent(begin);
        searchPanel.addComponent(stop);
        searchPanel.addComponent(buttonSearch);
        searchPanel.setComponentAlignment(buttonSearch, Alignment.BOTTOM_LEFT);
        addComponent(searchPanel);

        AtomicReference<Page<ThingDto>> thingPage = new AtomicReference<>(thingService.findAll(PageRequest.of(0, PAGE_SIZE)));
        Grid<ThingDto> thingsGrid = new Grid<>();

        buttonSearch.addClickListener(clickEvent -> {
            List<ThingDto> allThingsByTypeFreeBetween = thingService.getAllThingsByTypeFreeBetween(type.getSelectedItem().get(), begin.getValue(), stop.getValue());
            thingsGrid.setItems(allThingsByTypeFreeBetween);

        });

        thingsGrid.setSizeFull();
        thingsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        thingsGrid.setItems(thingPage.get().getContent());
        thingsGrid.addColumn(ThingDto::getName).setCaption("Name");
        thingsGrid.addColumn(ThingDto::getType).setCaption("Type");
        thingsGrid.addColumn(ThingDto::getDeposit).setCaption("Deposit");
        thingsGrid.addColumn(ThingDto::getStatus).setCaption("Status");
        thingsGrid.addColumn(ThingDto::getPriceForDay).setCaption("Day");
        thingsGrid.addColumn(ThingDto::getPriceForWeek).setCaption("Week");
        thingsGrid.addColumn(ThingDto::getPriceForTwoWeeks).setCaption("Two weeks");
        thingsGrid.addColumn(ThingDto::getPriceForMonth).setCaption("Month");
        thingsGrid.setHeightByRows(PAGE_SIZE);
        addComponent(thingsGrid);
        setExpandRatio(thingsGrid, 1f);


        buttonSearch.addClickListener(clickEvent -> {
            List<ThingDto> allThingsByTypeFreeBetween = thingService.getAllThingsByTypeFreeBetween(type.getSelectedItem().get(), begin.getValue(), stop.getValue());
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

        buttonNewThing.addClickListener(clickEvent -> Utils.newClickListenerSupplier.accept(this::getUI));

        buttonNewOrder.addClickListener(clickEvent -> {
            String s = OrderView.VIEW_NAME + "/new/"
                    + thingsGrid.getSelectedItems().toArray(new ThingDto[0])[0].getId()
                    + "/" + begin.getValue() + "/" + stop.getValue();
            getUI().getNavigator().navigateTo(s);
        });

        buttonDetails.addClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(thingsGrid, this::getUI, ThingView.VIEW_NAME));

        addComponent(buttons);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null && !event.getParameters().isEmpty()) {
            Notification.show(event.getParameters());
        }
    }
}
