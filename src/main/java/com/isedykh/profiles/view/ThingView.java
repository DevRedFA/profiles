package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.OrderService;
import com.isedykh.profiles.service.TermService;
import com.isedykh.profiles.service.ThingService;
import com.isedykh.profiles.service.ThingStatusService;
import com.isedykh.profiles.service.ThingTypeService;
import com.isedykh.profiles.service.entity.Order;
import com.isedykh.profiles.service.entity.Price;
import com.isedykh.profiles.service.entity.Term;
import com.isedykh.profiles.service.entity.Thing;
import com.isedykh.profiles.service.entity.ThingStatus;
import com.isedykh.profiles.service.entity.ThingType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.isedykh.profiles.view.ViewUtils.getOrderGridWithSettings;

@RequiredArgsConstructor
@SpringView(name = ThingView.VIEW_NAME)
@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
public class ThingView extends VerticalLayout implements View {

    static final String VIEW_NAME = "thing";

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
    private final TextArea comments = new TextArea("Comments");
    private final Grid<Price> pricesGrind = new Grid<>();
    private final Image image = new Image();
    private final Calendar<BasicItem> calendar = new Calendar<>("Calendar");
    private final Grid<Order> ordersGrid = getOrderGridWithSettings();
    private final Label actualPriceSum = new Label("Actual prices sum: ");

    @PostConstruct
    public void init() {

        purchaseDate.setDateFormat(Utils.DATE_FORMAT);

        Button buttonAddOrder = new Button("New order");
        buttonAddOrder.addClickListener(event -> {
            saveThing();
            String s = OrderView.VIEW_NAME + "/new/thing=" + thing.getId();
            getUI().getNavigator().navigateTo(s);
        });

        comments.setWidth("100%");
        comments.setHeight(10, Unit.EX);

        pricesGrind.setSelectionMode(Grid.SelectionMode.SINGLE);
        pricesGrind.getEditor().setEnabled(true);

        image.setVisible(false);


        // TODO: 07.07.2018 Extract from Client and Thing view
        class ImageReceiver implements Upload.Receiver, Upload.SucceededListener {
            private static final long serialVersionUID = -1276759102490466761L;

            private File file;

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                FileOutputStream fos;
                try {
                    file = new File(Utils.START_POINT + thing.getName().replace(" ", "_")
                            + "_" + thing.getPurchaseDate().toString());
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

        HorizontalLayout nameDateDetails = new HorizontalLayout();
        nameDateDetails.addComponent(name);
        nameDateDetails.addComponent(purchaseDate);

        Button addThingType = new Button("Add new type");
        addThingType.addClickListener(event -> {
            WindowTemplate<ThingType> sub = new WindowTemplate<>(ThingType.class, thingTypeService);
            UI.getCurrent().addWindow(sub);
        });

        HorizontalLayout typeDetails = new HorizontalLayout();
        typeDetails.addComponent(type);
        typeDetails.addComponent(addThingType);
        typeDetails.setComponentAlignment(addThingType, Alignment.BOTTOM_CENTER);

        Button addThingStatus = new Button("Add new status");
        addThingStatus.addClickListener(event -> {
            WindowTemplate<ThingStatus> sub = new WindowTemplate<>(ThingStatus.class, thingStatusService);
            UI.getCurrent().addWindow(sub);
        });

        HorizontalLayout statusDetails = new HorizontalLayout();
        statusDetails.addComponent(status);
        statusDetails.addComponent(addThingStatus);
        statusDetails.setComponentAlignment(addThingStatus, Alignment.BOTTOM_CENTER);

        HorizontalLayout priceDepositDetails = new HorizontalLayout();
        priceDepositDetails.addComponent(purchasePrice);
        priceDepositDetails.addComponent(deposit);

        Button saveThing = new Button("Save thing");
        saveThing.addClickListener(event -> saveThing());

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponent(saveThing);
        buttonsLayout.addComponent(buttonAddOrder);
        buttonsLayout.setWidth("100%");

        VerticalLayout thingDetails = new VerticalLayout();
        thingDetails.addComponent(nameDateDetails);
        thingDetails.addComponent(typeDetails);
        thingDetails.addComponent(statusDetails);
        thingDetails.addComponent(priceDepositDetails);
        thingDetails.addComponent(comments);
        thingDetails.addComponent(actualPriceSum);
        thingDetails.addComponent(buttonsLayout);
        thingDetails.setWidth("100%");

        Button addTerm = new Button("Add new term");
        addTerm.addClickListener(event -> {
            TermTemplate sub = new TermTemplate(termService);
            UI.getCurrent().addWindow(sub);
        });

        VerticalLayout priceDetails = new VerticalLayout();
        priceDetails.addComponent(pricesGrind);
        priceDetails.addComponent(addTerm);

        VerticalLayout imageDetails = new VerticalLayout();
        imageDetails.addComponent(image);
        imageDetails.addComponent(upload);

        HorizontalLayout fullDetails = new HorizontalLayout();
        fullDetails.addComponent(thingDetails);
        fullDetails.addComponent(priceDetails);
        fullDetails.addComponent(imageDetails);
        fullDetails.setExpandRatio(thingDetails, 2f);
        fullDetails.setExpandRatio(priceDetails, 2f);
        fullDetails.setExpandRatio(imageDetails, 3f);
        fullDetails.setWidth("100%");

        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(fullDetails, "Details");
        tabSheet.addTab(calendar, "Dates");
        tabSheet.addTab(ordersGrid, "Orders");

        ordersGrid.addItemClickListener(clickEvent -> Utils.getDetailsDoubleClickListenerSupplier(clickEvent, this::getUI, OrderView.VIEW_NAME));


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
                // ignoring exception
            }
        });


        addComponent(tabSheet);


    }

    private void saveThing() {
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
                    BasicItemProvider<BasicItem> itemProvider = new BasicItemProvider<BasicItem>() {
                    };
                    itemProvider.setItems(items);
                    calendar.setDataProvider(itemProvider);
                    ordersGrid.setItems(orderService.getThingOrderHistory(thing));
                    actualPriceSum.setValue("Actual prices sum: "+ thingService.countAllActualPrices(thing)/100);
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
