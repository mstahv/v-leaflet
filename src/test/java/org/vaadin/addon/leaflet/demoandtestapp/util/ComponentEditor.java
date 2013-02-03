package org.vaadin.addon.leaflet.demoandtestapp.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;

/**
 * A simple helper to edit properties in component hierarchy during testing.
 */
class ComponentEditor extends Window {
    private Tree tree;
    private HorizontalSplitPanel horizontalSplitPanel;

    public ComponentEditor(ComponentContainer root) {
        setCaption("Component editor");
        setWidth("600px");
        setHeight("250px");
        setPositionX(300);
        setPositionY(100);

        horizontalSplitPanel = new HorizontalSplitPanel();
        horizontalSplitPanel.setSplitPosition(30);
        setContent(horizontalSplitPanel);
        tree = new Tree();
        horizontalSplitPanel.setFirstComponent(tree);
        add(root);
        tree.setSelectable(true);
        tree.setImmediate(true);
        tree.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Component c = (Component) event.getProperty().getValue();
                setEditedComponent(c);
            }
        });
        tree.expandItemsRecursively(root);
    }

    static Collection<String> noneditablefields = Arrays.asList(
            "componentError", "data", "debugId", "errorHandler", "icon",
            "parent", "propertyDataSource", "type", "window");

    protected void setEditedComponent(Component c) {
        Form form = new Form();
        form.setImmediate(true);
        form.setFormFieldFactory(new FormFieldFactory() {

            public Field createField(Item item, Object propertyId,
                    Component uiContext) {
                if (noneditablefields.contains(propertyId))
                    return null;
                return DefaultFieldFactory.get().createField(item, propertyId,
                        uiContext);

            }
        });
        BeanItem beanItem = new BeanItem(c);
        form.setItemDataSource(beanItem);
        horizontalSplitPanel.setSecondComponent(form);
    }

    public void add(ComponentContainer root) {
        tree.addItem(root);
        setTreeCaption(root);
        Iterator<Component> componentIterator = root.getComponentIterator();
        while (componentIterator.hasNext()) {
            Component next = componentIterator.next();
            if (next instanceof ComponentContainer) {
                add((ComponentContainer) next);
            } else {
                tree.addItem(next);
                setTreeCaption(next);
                if (next instanceof Form) {
                    Layout layout = ((Form) next).getLayout();
                    add(layout);
                    tree.setParent(layout, next);
                    layout = ((Form) next).getFooter();
                    add(layout);
                    tree.setParent(layout, next);
                }
            }
            tree.setParent(next, root);
        }

    }

    private void setTreeCaption(Component root) {
        tree.setItemCaption(root, root.getClass().getSimpleName());
    }
}