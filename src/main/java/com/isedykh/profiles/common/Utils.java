package com.isedykh.profiles.common;

import com.isedykh.profiles.service.CrudService;
import com.isedykh.profiles.service.entity.Identifiable;
import com.vaadin.ui.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Utils {

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final int PAGE_SIZE = 15;

    private Utils() {
    }

    public static void getDetailsDoubleClickListenerSupplier(Grid.ItemClick<? extends Identifiable> clickEvent, Supplier<UI> uiSupplier, String to) {
        if (clickEvent.getMouseEventDetails().isDoubleClick()) {
            Identifiable identifiable = clickEvent.getItem();
            uiSupplier.get().getNavigator().navigateTo(to + "/" + identifiable.getId());
        }
    }

    public static void getDetailsDoubleClickListenerSupplier(Grid<? extends Identifiable> grid, Supplier<UI> uiSupplier, String to) {
        Set selectedItems = grid.getSelectedItems();
        if (selectedItems.size() == 1) {
            Identifiable identifiable = Identifiable.class.cast(selectedItems.toArray()[0]);
            uiSupplier.get().getNavigator().navigateTo(to + "/" + identifiable.getId());
        }
    }

    public static void getNewClickListenerSupplier(Supplier<UI> uiSupplier, String to) {
        uiSupplier.get().getNavigator().navigateTo(to + "/new");
    }


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
    public static <T> Button.ClickListener getDeleteClickListener(Grid<T> grid,Supplier<UI> uiSupplier,
                                                                  CrudService<T> crudService) {
        return clickEvent -> {
            Set selectedItems = grid.getSelectedItems();
            if (selectedItems.size() == 1) {
                crudService.delete((T) (selectedItems.toArray()[0]));
            }
            uiSupplier.get().getPage().reload();
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
