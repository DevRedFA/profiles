package com.isedykh.profiles.view;

import com.isedykh.profiles.common.Utils;
import com.isedykh.profiles.service.OrderService;
import com.isedykh.profiles.service.ThingTypeService;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import lombok.RequiredArgsConstructor;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.client.shared.Resolution;
import org.tltv.gantt.client.shared.Step;
import org.tltv.gantt.client.shared.SubStep;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@SpringView(name = GanttOrdersView.VIEW_NAME)
@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
public class GanttOrdersView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ganttOrders";

    private final ThingTypeService thingTypeService;
    private final OrderService orderService;

    private Gantt gantt;

    @PostConstruct
    public void init() {

        ComboBox<ThingType> type = new ComboBox<>("Type");
        Utils.setFieldIfNotNull(thingTypeService::findAll, type::setItems, s -> s);

        Button buttonSearch = new Button("Show");

        buttonSearch.addClickListener(clickEvent -> {
            gantt.removeSteps();
            type.getSelectedItem().ifPresent(this::addOrdersToGantt);
        });

        DateField begin = new DateField("Begin");
        begin.setDateFormat(Utils.DATE_FORMAT);

        DateField stop = new DateField("End");
        stop.setDateFormat(Utils.DATE_FORMAT);

        HorizontalLayout searchPanel = new HorizontalLayout();
        searchPanel.addComponent(type);
        searchPanel.addComponent(begin);
        searchPanel.addComponent(stop);
        searchPanel.addComponent(buttonSearch);
        searchPanel.setComponentAlignment(buttonSearch, Alignment.BOTTOM_LEFT);

        gantt = new Gantt();
        gantt.setSizeFull();
        addComponent(searchPanel);
        addComponent(gantt);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            List<ThingType> allTypes = thingTypeService.findAll();
            allTypes.stream().filter(s -> s.getName()
                    .equals(event.getParameters()))
                    .findAny()
                    .ifPresent(this::addOrdersToGantt);
        }

        gantt.setStartDate(Date.valueOf(LocalDate.now().minusMonths(1)));
        gantt.setEndDate(Date.valueOf(LocalDate.now().plusMonths(3)));
        gantt.setResolution(Resolution.Day);
        gantt.setResizableSteps(false);
        gantt.setMovableSteps(false);
    }

    private void addOrdersToGantt(ThingType type) {
        List<Order> allOrdersWithThingType = orderService.getAllOrdersWithThingType(type);
        createGanttSteps(allOrdersWithThingType).forEach(gantt::addStep);
    }

    private List<Step> createGanttSteps(List<Order> orders) {
        ArrayList<Step> steps = new ArrayList<>();
        Map<Thing, List<Order>> collect = orders.stream().collect(Collectors.groupingBy(Order::getThing));
        collect.forEach((thing, thingOrders) ->
        {
            Step step = new Step("");
            step.setDescription(thing.getName());
            SubStep initSubStep = new SubStep("init");
            initSubStep.setStartDate(Date.valueOf(LocalDate.now().minusMonths(1).minusDays(1)));
            initSubStep.setEndDate(Date.valueOf(LocalDate.now().minusMonths(1)));

            SubStep endSubStep = new SubStep("end");
            endSubStep.setStartDate(Date.valueOf(LocalDate.now().plusMonths(3).plusDays(1)));
            endSubStep.setEndDate(Date.valueOf(LocalDate.now().plusMonths(3).plusDays(2)));


            List<SubStep> subSteps = thingOrders.stream().map(order -> {
                SubStep subStep = new SubStep("Order id: " + order.getId());
                subStep.setStartDate(Date.valueOf(order.getBegin()));
                subStep.setDescription(getOrderDescription(order));
                subStep.setEndDate(Date.valueOf(order.getStop()));
                return subStep;
            }).collect(Collectors.toList());

            subSteps.add(initSubStep);
            subSteps.add(endSubStep);
            step.addSubSteps(subSteps);
            steps.add(step);
        });

        return steps;
    }

    private String getOrderDescription(Order order) {
        return "Id: " + order.getId() + System.lineSeparator() +
                "Thing: " + order.getThing().getName() + System.lineSeparator() +
                "Client: " + order.getClient().getName() + System.lineSeparator() +
                "Start: " + order.getBegin() + " End: " + order.getStop();
    }
}
