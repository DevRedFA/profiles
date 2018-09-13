package com.isedykh.profiles.view;

import com.isedykh.profiles.service.TermService;
import com.isedykh.profiles.service.entity.Term;
import com.vaadin.data.Binder;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.AbstractStringToNumberConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.PostConstruct;

import static com.isedykh.profiles.common.Utils.PAGE_SIZE;

@RequiredArgsConstructor
@SpringView(name = TermsView.VIEW_NAME)
@SuppressWarnings({"squid:S1948", "squid:MaximumInheritanceDepth", "squid:S2160"})
public class TermsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "terms";

    private final TermService termService;

    @PostConstruct
    public void init() {

        Page<Term> termPage = termService.findAll(PageRequest.of(0, PAGE_SIZE));
        Grid<Term> termGrid = new Grid<>();
        termGrid.setSizeFull();
        TextField value = new TextField();
        Binder<Term> binder = termGrid.getEditor().getBinder();
        Binder.Binding<Term, Double> bindingCoefficient = binder.forField(value)
                .withConverter(new AbstractStringToNumberConverter<Double>(0.0, "Error") {
                    @Override
                    public Result<Double> convertToModel(String value, ValueContext context) {
                        final double i = Double.parseDouble(value);
                        if (i < 0 || i > 1) {
                            return Result.error("Coefficient must me positive and less then 1");
                        } else {
                            return Result.ok(i * 100);
                        }
                    }
                })
                .bind(term -> (double) term.getCoefficient() / 100,
                        (term, aDouble) -> term.setCoefficient(aDouble.intValue())
                );
        termGrid.addColumn(Term::getName).setCaption("Name");
        termGrid.addColumn(term -> ((double) term.getCoefficient()) / 100).setCaption("Coefficient").setEditorBinding(bindingCoefficient);
        termGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        termGrid.setHeightByRows(PAGE_SIZE);
        termGrid.setItems(termPage.getContent());
        termGrid.getEditor().setEnabled(true);
        termGrid.getEditor().addSaveListener(event -> {
            termService.save(event.getBean());
            getUI().getPage().reload();
        });
        Button addTerm = new Button("Add new term");
        addTerm.addClickListener(event -> {
            TermTemplate sub = new TermTemplate(termService);
            UI.getCurrent().addWindow(sub);
        });
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponent(addTerm);
        addComponent(termGrid);
        addComponent(buttons);
        setExpandRatio(termGrid, 1f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // do nothing
    }
}
