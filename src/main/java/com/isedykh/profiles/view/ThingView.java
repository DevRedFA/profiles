package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.dao.entity.Term;
import com.isedykh.profiles.dao.entity.ThingStatus;
import com.isedykh.profiles.dao.entity.ThingType;
import com.isedykh.profiles.service.Price;
import com.isedykh.profiles.service.Thing;
import com.isedykh.profiles.service.ThingService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@SpringView(name = ThingView.VIEW_NAME)
public class ThingView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "thing";

    @Autowired
    private final ThingService thingService;

    private Thing thing;

    List<Price> prices;

    private TextField name = new TextField("Name");

    private ComboBox<ThingType> type = new ComboBox<>("Type");

    private ComboBox<ThingStatus> status = new ComboBox<>("Status");

    private TextField purchasePrice = new TextField("Purchase price");

    private DateField purchaseDate = new DateField("Purchase date");

    private TextField deposit = new TextField("Deposit");

    private TextField comments = new TextField("Comments");

    private VerticalLayout verticalLayout = new VerticalLayout();

    private HorizontalLayout horizontalLayout = new HorizontalLayout();

    private Grid<Price> pricesGrind = new Grid<>();

    private Button buttonAddOrder = new Button("New order");

    private Button saveThing = new Button("Save thing");

    @PostConstruct
    public void init() {
        addComponent(new Label("Detail thing view"));
        verticalLayout.addComponent(name);
        verticalLayout.addComponent(type);
        verticalLayout.addComponent(status);
        verticalLayout.addComponent(purchasePrice);
        verticalLayout.addComponent(purchaseDate);
        verticalLayout.addComponent(saveThing);
        pricesGrind.setSelectionMode(Grid.SelectionMode.SINGLE);
        pricesGrind.getEditor().setEnabled(true);
        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(comments);
        horizontalLayout.addComponent(pricesGrind);
        horizontalLayout.addComponent(buttonAddOrder);
        addComponent(horizontalLayout);

        buttonAddOrder.addClickListener(event -> {
            String s = OrderView.VIEW_NAME + "/new/" + thing.getId();
            getUI().getNavigator().navigateTo(s);
        });

        purchasePrice.addValueChangeListener(event -> {
            try {
                int i = Integer.parseInt(event.getValue()) * 100;
                prices = new ArrayList<>();
                prices.add(new Price(null, Term.DAY, (int) (i * Price.DAY_COEFFICIENT / 100)));
                prices.add(new Price(null, Term.WEEK, (int) (i * Price.WEEK_COEFFICIENT / 100)));
                prices.add(new Price(null, Term.TWO_WEEKS, (int) (i * Price.TWO_WEEKS_COEFFICIENT / 100)));
                prices.add(new Price(null, Term.MONTH, (int) (i * Price.MONTH_COEFFICIENT / 100)));
                pricesGrind.setItems(prices);
                thing.setPrices(prices);
                thing.setPurchasePrice(i);
            } catch (NumberFormatException ignored) {
            }
        });

        saveThing.addClickListener(event -> {
            thing.setName(name.getValue());
            thing.setDeposit(Integer.parseInt(deposit.getValue()));
            thing.setPurchaseDate(purchaseDate.getValue());
            Optional<ThingType> selectedItem = type.getSelectedItem();
            if (selectedItem.isPresent()) {
                thing.setType(selectedItem.get());
            } else {
                Notification.show("Choose thing type");
            }
            status.getSelectedItem().ifPresent(thingStatus -> thing.setStatus(thingStatus));
            thing.setComments(comments.getValue());

            thingService.save(thing);
        });

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            if (event.getParameters().contains("new")) {
                thing = new Thing();
                thing.setStatus(ThingStatus.FREE);
                status.setSelectedItem(ThingStatus.FREE);

                thing.setPurchaseDate(LocalDate.now());
                purchaseDate.setValue(LocalDate.now());


                prices = new ArrayList<>();
                prices.add(new Price(null, Term.DAY, 0));
                prices.add(new Price(null, Term.WEEK, 0));
                prices.add(new Price(null, Term.TWO_WEEKS, 0));
                prices.add(new Price(null, Term.MONTH, 0));
                pricesGrind.setItems(prices);
            } else {
                int id = Integer.parseInt(event.getParameters());
                try {
                    thing = thingService.getById(id);
                } catch (Exception e) {
                    Notification.show("Thing with such id not found");
                }
            }
        }

        Utils.setFieldIfNotNull(thing::getName, name::setValue, s -> s);
        Utils.setFieldIfNotNull(thing::getPurchasePrice, purchasePrice::setValue, String::valueOf);
        Utils.setFieldIfNotNull(thing::getDeposit, deposit::setValue, String::valueOf);
        Utils.setFieldIfNotNull(thing::getPurchaseDate, purchaseDate::setValue, s -> s);
        Utils.setFieldIfNotNull(thing::getComments, comments::setValue, s -> s);

        Utils.setFieldIfNotNull(ThingType::values, type::setItems, s -> s);
        Utils.setFieldIfNotNull(thing::getType, type::setSelectedItem, s -> s);

        Utils.setFieldIfNotNull(ThingStatus::values, status::setItems, s -> s);
        Utils.setFieldIfNotNull(thing::getStatus, status::setSelectedItem, s -> s);

        Utils.setFieldIfNotNull(thing::getPrices, pricesGrind::setItems, s -> s);

        pricesGrind.addColumn(Price::getTerm).setCaption("Terms");
        pricesGrind.addColumn(Price::getPriceValue).setCaption("Price");
        pricesGrind.setHeightByRows(4);
    }
}
