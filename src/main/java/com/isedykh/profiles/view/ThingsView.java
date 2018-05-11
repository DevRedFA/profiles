package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.dao.entity.ThingType;
import com.isedykh.profiles.mapper.ThingMapper;
import com.isedykh.profiles.service.Thing;
import com.isedykh.profiles.service.ThingDto;
import com.isedykh.profiles.service.ThingDtoService;
import com.isedykh.profiles.service.ThingService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.isedykh.profiles.common.Utils.getPageChangeClickListener;

@AllArgsConstructor
@SpringView(name = ThingsView.VIEW_NAME)
public class ThingsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "things";

    private ThingDtoService thingService;

    private  ThingMapper thingMapper;

    @PostConstruct
    public void init() {

        AtomicReference<Page<ThingDto>> thingPage = new AtomicReference<>(thingService.findAll(PageRequest.of(0, 17)));
        Grid<ThingDto> thingsGrid = new Grid<>();
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
        thingsGrid.setHeightByRows(17);
        addComponent(thingsGrid);
        setExpandRatio(thingsGrid, 1f);

        Button buttonSearch = new Button("Search");
        DateField begin = new DateField("Begin");
        DateField end = new DateField("End");
        ComboBox<ThingType> type = new ComboBox<>("Type");
        Utils.setFieldIfNotNull(ThingType::values, type::setItems, s -> s);

        buttonSearch.addClickListener(clickEvent ->{
            List<ThingDto> allThingsByTypeFreeBetween = thingService.getAllThingsByTypeFreeBetween(type.getSelectedItem().get(), begin.getValue(), end.getValue());
            thingsGrid.setItems(allThingsByTypeFreeBetween);

        });

        thingsGrid.addItemClickListener(clickEvent -> Utils.detailsDoubleClickListenerSupplier.accept(clickEvent, this::getUI));

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

        buttonNext.addClickListener(getPageChangeClickListener(thingPage, Slice::nextPageable, thingsGrid, buttonNext, buttonPrevious, thingService));

        buttonPrevious.addClickListener(getPageChangeClickListener(thingPage, Slice::previousPageable, thingsGrid, buttonNext, buttonPrevious, thingService));

        buttonNew.addClickListener(clickEvent -> Utils.newClickListenerSupplier.accept(this::getUI));

        buttonDetails.addClickListener(clickEvent -> Utils.detailsClickListenerSupplier.accept(thingsGrid, this::getUI));

        addComponent(buttons);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
