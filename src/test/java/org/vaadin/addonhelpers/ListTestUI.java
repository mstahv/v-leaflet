package org.vaadin.addonhelpers;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.vaadin.addon.leaflet.testbenchtests.SimpleTest;

import java.util.stream.Collectors;

/**
 * Lists all installed test UIs
 * @author elmot
 */
public class ListTestUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        Grid<Class<? extends AbstractTest>> testsGrid = new Grid<>();
        testsGrid.setSizeFull();
        testsGrid.setItems(SimpleTest.getAllTestClasses().collect(Collectors.toList()));
        testsGrid.addColumn(Class::getName, new ButtonRenderer<>(event->
        getPage().setLocation("/" + event.getItem().getName()))).setCaption("Class Name");
        testsGrid.addColumn(clazz -> {
            try {
                return clazz.newInstance().getDescription();
            } catch (Throwable e) {
                return e.getMessage();
            }
        }).setCaption("Description");
        setContent(new VerticalLayout(testsGrid));
    }
}
