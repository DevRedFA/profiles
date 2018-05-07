package com.isedykh.profiles.common;

import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.Identifiable;
import com.isedykh.profiles.service.PageableService;
import com.isedykh.profiles.view.OrderView;
import com.vaadin.ui.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class Utils {

    public static HorizontalLayout initTailMenu(Grid grid, UI ui) {
        HorizontalLayout buttons = new HorizontalLayout();
        Button buttonNext = new Button("Next");
        Button buttonPrivious = new Button("Previous");
        Button buttonDetails = new Button("Details");
        Button buttonNew = new Button("New");

        buttons.addComponent(buttonPrivious);
        buttons.addComponent(buttonDetails);
        buttons.addComponent(buttonNew);
        buttons.addComponent(buttonNext);

        buttonNew.addClickListener(clickEvent -> {
            String s = ui.getNavigator().getState() + "/new";
            ui.getNavigator().navigateTo(s);
        });

        buttonDetails.addClickListener(clickEvent -> {
            Set selectedItems = grid.getSelectedItems();
            if (selectedItems.size() == 1) {
                Object[] objects = selectedItems.toArray();
                Identifiable obj = Identifiable.class.cast(objects[0]);
                String state = ui.getNavigator().getState();
                String s = state.substring(0, state.length() - 1) + "/" + obj.getId();
                ui.getNavigator().navigateTo(s);
            } else {
                Notification.show("Please select one option");
            }
        });
        return buttons;
    }

    public void constructClickListener(Grid.ItemClick<? extends Identifiable> clickEvent, UI ui) {
        if (clickEvent.getMouseEventDetails().isDoubleClick()) {
            Identifiable order = clickEvent.getItem();
            String s = OrderView.VIEW_NAME + "/" + order.getId();
            ui.getNavigator().navigateTo(s);
        }
    }

    public static <T> Button.ClickListener getPageChangeClickListener(AtomicReference<Page<T>> page, Function<Page<T>, Pageable> nextPageable, Grid<T> grid, Button buttonNext, Button buttonPrevious, PageableService<T> pageableService) {
        return clickEvent -> {
            page.set(pageableService.findAll(nextPageable.apply(page.get())));
            grid.setItems(page.get().getContent());
            buttonPrevious.setEnabled(!page.get().isFirst());
            buttonNext.setEnabled(true);
        };
    }
}
