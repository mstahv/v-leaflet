package org.vaadin.addon.vodatime.demoandtestapp;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractTest extends UI {

    private VerticalLayout content;

    public AbstractTest() {
        content = new VerticalLayout();
        setContent(content);
    }

    protected void setup() {
        Component map = getTestComponent();
        content.setSizeFull();
        content.addComponent(map);
        content.setExpandRatio(map, 1);
    }

    abstract Component getTestComponent();

    @Override
    protected void init(VaadinRequest request) {
        setup();
        Notification.show(getDescription(),
                Notification.Type.WARNING_MESSAGE);
    }
    
    

}
