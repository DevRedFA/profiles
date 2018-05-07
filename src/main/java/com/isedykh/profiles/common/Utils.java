package com.isedykh.profiles.common;

import com.isedykh.profiles.service.Identifiable;
import com.isedykh.profiles.service.PageableService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Utils {

    private Utils() {
    }

    private static final BiConsumer<Identifiable, Supplier<UI>>
            baseClickListenerSupplier = (identifiable, uiSupplier) -> {
        String state = uiSupplier.get().getNavigator().getState();
        String s = state.substring(0, state.length() - 1) + "/" + identifiable.getId();
        uiSupplier.get().getNavigator().navigateTo(s);
    };

    public static final BiConsumer<Grid<? extends Identifiable>, Supplier<UI>>
            detailsClickListenerSupplier = (grid, uiSupplier) -> {
        Set selectedItems = grid.getSelectedItems();
        if (selectedItems.size() == 1) {
            Identifiable obj = Identifiable.class.cast(selectedItems.toArray()[0]);
            baseClickListenerSupplier.accept(obj, uiSupplier);
        } else {
            Notification.show("Please select one option");
        }
    };

    public static final BiConsumer<Grid.ItemClick<? extends Identifiable>, Supplier<UI>>
            detailsDoubleClickListenerSupplier = (clickEvent, uiSupplier) -> {
        if (clickEvent.getMouseEventDetails().isDoubleClick()) {
            Identifiable obj = clickEvent.getItem();
            baseClickListenerSupplier.accept(obj, uiSupplier);
        }
    };

    public static final Consumer<Supplier<UI>> newClickListenerSupplier = uiSupplier -> {
        String state = uiSupplier.get().getNavigator().getState();
        String s = state.substring(0, state.length() - 1) + "/new";
        uiSupplier.get().getNavigator().navigateTo(s);
    };

    public static <T> Button.ClickListener getPageChangeClickListener(AtomicReference<Page<T>> page,
                                                                      Function<Page<T>, Pageable> nextPageable,
                                                                      Grid<T> grid,
                                                                      Button buttonNext,
                                                                      Button buttonPrevious,
                                                                      PageableService<T> pageableService) {
        return clickEvent -> {
            page.set(pageableService.findAll(nextPageable.apply(page.get())));
            grid.setItems(page.get().getContent());
            buttonPrevious.setEnabled(!page.get().isFirst());
            buttonNext.setEnabled(true);
        };
    }
}
