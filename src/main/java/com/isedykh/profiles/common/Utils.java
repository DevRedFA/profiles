package com.isedykh.profiles.common;

import com.isedykh.profiles.service.Identifiable;
import com.isedykh.profiles.service.CrudService;
import com.vaadin.ui.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.Collectors;

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
            Identifiable identifiable = Identifiable.class.cast(selectedItems.toArray()[0]);
            baseClickListenerSupplier.accept(identifiable, uiSupplier);
        } else {
            Notification.show("Please select one option");
        }
    };

    public static final BiConsumer<Grid.ItemClick<? extends Identifiable>, Supplier<UI>>
            detailsDoubleClickListenerSupplier = (clickEvent, uiSupplier) -> {
        if (clickEvent.getMouseEventDetails().isDoubleClick()) {
            Identifiable identifiable = clickEvent.getItem();
            baseClickListenerSupplier.accept(identifiable, uiSupplier);
        }
    };

    public static final Consumer<Supplier<UI>> newClickListenerSupplier = uiSupplier -> {
        String state = uiSupplier.get().getNavigator().getState();
        String s = state.substring(0, state.length() - 1) + "/new";
        uiSupplier.get().getNavigator().navigateTo(s);
    };

    public static <T> Button.ClickListener getPageChangeClickListener(AtomicReference<Page<T>> page,
                                                                      Function<Page<T>, Pageable> changePageFunction,
                                                                      Grid<T> grid,
                                                                      Button buttonNext,
                                                                      Button buttonPrevious,
                                                                      CrudService<T> crudService,
                                                                      ComboBox<String> nameFilter,
                                                                      Function<T, String> comboBoxTransfer
    ) {
        return clickEvent -> {
            page.set(crudService.findAll(changePageFunction.apply(page.get())));
            grid.setItems(page.get().getContent());
            buttonPrevious.setEnabled(!page.get().isFirst());
            buttonNext.setEnabled(!page.get().isLast());
            nameFilter.setItems(page.get().getContent().stream().map(comboBoxTransfer));
        };
    }

    public static <T> Button.ClickListener getPageChangeClickListener(AtomicReference<Page<T>> page,
                                                                      Function<Page<T>, Pageable> changePageFunction,
                                                                      Grid<T> grid,
                                                                      Button buttonNext,
                                                                      Button buttonPrevious,
                                                                      CrudService<T> crudService
    ) {
        return clickEvent -> {
            page.set(crudService.findAll(changePageFunction.apply(page.get())));
            grid.setItems(page.get().getContent());
            buttonPrevious.setEnabled(!page.get().isFirst());
            buttonNext.setEnabled(!page.get().isLast());
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Button.ClickListener getDeleteClickListener(Grid<T> grid,
                                                                  CrudService<T> crudService) {
        return clickEvent -> {
            Set selectedItems = grid.getSelectedItems();
            if (selectedItems.size() == 1) {
                crudService.delete((T) (selectedItems.toArray()[0]));
            }
        };
    }

    public static <T, R> void setFieldIfNotNull(Supplier<T> condition, Consumer<R> field, Function<T, R> converter) {
        if (condition != null) {
            T t = condition.get();
            if (t != null) {
                field.accept(converter.apply(t));
            }
        }
    }
}
