package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.OrderService;
import com.isedykh.profiles.service.TermService;
import com.isedykh.profiles.service.ThingService;
import com.isedykh.profiles.service.ThingTypeService;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.entity.Price;
import com.isedykh.profiles.service.entity.Term;
import com.isedykh.profiles.service.entity.Thing;
import com.isedykh.profiles.service.entity.ThingStatus;
import com.isedykh.profiles.service.entity.ThingType;
import com.isedykh.profiles.service.ThingStatusService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItem;
import org.vaadin.addon.calendar.item.BasicItemProvider;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@SpringView(name = ThingView.VIEW_NAME)
public class ThingView extends VerticalLayout implements View {

    static final String VIEW_NAME = "thing";
    public static final String START_POINT = "./";

    private final ThingService thingService;

    private final ThingTypeService thingTypeService;

    private final ThingStatusService thingStatusService;

    private final OrderService orderService;

    private final TermService termService;

    private Thing thing;

    private List<Price> prices;

    private final TextField name = new TextField("Name");

    private final ComboBox<ThingType> type = new ComboBox<>("Type");

    private final ComboBox<ThingStatus> status = new ComboBox<>("Status");

    private final TextField purchasePrice = new TextField("Purchase price");

    private final DateField purchaseDate = new DateField("Purchase date");

    private final TextField deposit = new TextField("Deposit");

    private final TextField comments = new TextField("Comments");

    private final VerticalLayout details = new VerticalLayout();

    private final VerticalLayout priceDetails = new VerticalLayout();

    private final HorizontalLayout fullDetails = new HorizontalLayout();

    private final HorizontalLayout imageDetails = new HorizontalLayout();

    private final HorizontalLayout typeDetails = new HorizontalLayout();

    private final HorizontalLayout statusDetails = new HorizontalLayout();

    private final Grid<Price> pricesGrind = new Grid<>();

    private final Button buttonAddOrder = new Button("New order");

    private final Button saveThing = new Button("Save thing");

    private final Button addThingType = new Button("Add new type");

    private final Button addThingStatus = new Button("Add new status");

    private final Button addTerm = new Button("Add new term");

    private final TabSheet tabSheet = new TabSheet();

    private final Image image = new Image();

    private final Calendar<BasicItem> calendar = new Calendar<>("Calendar");

    private BasicItemProvider<BasicItem> itemProvider = new BasicItemProvider<BasicItem>() {
        @Override
        public void setItems(Collection<BasicItem> basicItems) {
            super.setItems(basicItems);
        }

        @Override
        public List<BasicItem> getItems(ZonedDateTime startDate, ZonedDateTime endDate) {
            return super.getItems(startDate, endDate);
        }

    };

    @PostConstruct
    public void init() {

        purchaseDate.setDateFormat(Utils.DATE_FORMAT);

        details.addComponent(name);

        details.addComponent(typeDetails);
        typeDetails.addComponent(type);
        typeDetails.addComponent(addThingType);
        typeDetails.setComponentAlignment(addThingType, Alignment.BOTTOM_CENTER);

        details.addComponent(statusDetails);
        statusDetails.addComponent(status);
        statusDetails.addComponent(addThingStatus);
        statusDetails.setComponentAlignment(addThingStatus, Alignment.BOTTOM_CENTER);

        details.addComponent(purchasePrice);
        details.addComponent(deposit);
        details.addComponent(purchaseDate);
        details.addComponent(comments);
        details.addComponent(saveThing);
        details.addComponent(buttonAddOrder);

        pricesGrind.setSelectionMode(Grid.SelectionMode.SINGLE);
        pricesGrind.getEditor().setEnabled(true);
        fullDetails.addComponent(details);
        fullDetails.addComponent(priceDetails);
        priceDetails.addComponent(pricesGrind);
        priceDetails.addComponent(addTerm);

        image.setVisible(false);


        class ImageReceiver implements Upload.Receiver, Upload.SucceededListener {
            private static final long serialVersionUID = -1276759102490466761L;

            private File file;

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                FileOutputStream fos;
                try {
                    file = new File(START_POINT + thing.getName().replace(" ", "_") + "_" + thing.getPurchaseDate().toString());
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file<br/>",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos;
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {
                image.setVisible(true);
                image.setSource(new FileResource(file));
                thing.setPathToPhoto(file.getAbsolutePath());
                thingService.save(thing);
            }
        }

        ImageReceiver receiver = new ImageReceiver();

        final Upload upload = new Upload("Upload it here", receiver);
        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);

        imageDetails.addComponent(image);
        imageDetails.addComponent(upload);

        tabSheet.addTab(fullDetails, "Details");
        tabSheet.addTab(calendar, "Dates");
        tabSheet.addTab(imageDetails, "Image");


        addComponent(tabSheet);

        buttonAddOrder.addClickListener(event -> {
            String s = OrderView.VIEW_NAME + "/new/thing=" + thing.getId();
            getUI().getNavigator().navigateTo(s);
        });

        addThingType.addClickListener(event -> {
            WindowTemplate<ThingType> sub = new WindowTemplate<>(ThingType.class, thingTypeService);
            UI.getCurrent().addWindow(sub);
        });

        addThingStatus.addClickListener(event -> {
            WindowTemplate<ThingStatus> sub = new WindowTemplate<>(ThingStatus.class, thingStatusService);
            UI.getCurrent().addWindow(sub);
        });

        addTerm.addClickListener(event -> {
            TermTemplate sub = new TermTemplate(termService);
            UI.getCurrent().addWindow(sub);
        });

        purchasePrice.addValueChangeListener(event -> {
            try {
                int purchasePriceInCents = Integer.parseInt(event.getValue()) * 100;
                prices = new ArrayList<>();
                List<Term> all = termService.findAll();
                all.forEach(term -> prices.add(new Price(term, purchasePriceInCents)));
                pricesGrind.setItems(prices);
                thing.setPrices(prices);
                thing.setPurchasePrice(purchasePriceInCents);
            } catch (NumberFormatException ignored) {
            }
        });

        saveThing.addClickListener(event -> {
            boolean readyToSave = false;
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
            if (!StringUtils.isEmpty(name.getValue())) {
                readyToSave = true;
            }
            if (readyToSave) {
                thing = thingService.save(thing);
                Notification.show("Thing saved");
            } else {
                Notification.show("Set all required fields: name, thing type");
            }
        });

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            if (event.getParameters().contains("new")) {
                thing = new Thing();
                thing.setStatus(thingService.getStatusByName("free"));
                status.setSelectedItem(thingService.getStatusByName("free"));

                thing.setPurchaseDate(LocalDate.now());
                purchaseDate.setValue(LocalDate.now());

            } else {
                int id = Integer.parseInt(event.getParameters());
                try {
                    thing = thingService.getById(id);
                    List<Order> thingOrderHistory = orderService.getThingOrderHistory(thing);
                    List<BasicItem> items = thingOrderHistory.stream().map(s ->
                            new BasicItem(s.getStatus().getName(), s.getComments(),
                                    s.getBegin().atStartOfDay(ZoneId.of("Europe/Moscow")),
                                    s.getStop().atStartOfDay(ZoneId.of("Europe/Moscow"))))
                            .collect(Collectors.toList());
                    itemProvider.setItems(items);
                    calendar.setDataProvider(itemProvider);

                } catch (Exception e) {
                    Notification.show("Thing with such id not found");
                }
            }
        }

        Utils.setFieldIfNotNull(thing::getPathToPhoto, image::setSource, s -> new FileResource(new File(s)));
        Utils.setFieldIfNotNull(thing::getName, name::setValue, s -> s);
        Utils.setFieldIfNotNull(thing::getPurchasePrice, purchasePrice::setValue, price -> String.valueOf(price / 100));
        Utils.setFieldIfNotNull(thing::getDeposit, deposit::setValue, String::valueOf);
        Utils.setFieldIfNotNull(thing::getPurchaseDate, purchaseDate::setValue, s -> s);
        Utils.setFieldIfNotNull(thing::getComments, comments::setValue, s -> s);
        Utils.setFieldIfNotNull(thingTypeService::findAll, type::setItems, s -> s);
        Utils.setFieldIfNotNull(thing::getType, type::setSelectedItem, s -> s);
        Utils.setFieldIfNotNull(thingService::getAllThingStatuses, status::setItems, s -> s);
        Utils.setFieldIfNotNull(thing::getStatus, status::setSelectedItem, s -> s);
        Utils.setFieldIfNotNull(thing::getPrices, pricesGrind::setItems, s -> s);

        image.setVisible(true);

        pricesGrind.addColumn(Price::getTerm).setCaption("Terms");
        pricesGrind.addColumn(price -> price.getPriceValue() / 100).setCaption("Price");
        pricesGrind.setHeightByRows(4);
    }
}
