package org.vaadin.addonhelpers;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.junit.Assert;

@Theme("valo")
public abstract class AbstractTest extends UI {

    protected VerticalLayout content;

    public AbstractTest() {
        content = new VerticalLayout();
        setContent(content);
        setErrorHandler(
                errorEvent -> Assert.fail(getClass().getName() + ":" + errorEvent.getThrowable().toString())
        );
    }

    protected void setup() {
        Component map = getTestComponent();
        setContentSize(content);
        content.addComponent(map);
        content.setExpandRatio(map, 1);
    }

    /**
     * Sets the size of the content. Override to change from
     * {@link Sizeable#setSizeFull()}
     *
     * @param content
     */
    public void setContentSize(Component content) {
        content.setSizeFull();
    }

    public abstract Component getTestComponent();

    @Override
    protected void init(VaadinRequest request) {
        setup();
        String description = getDescription();
        if (description != null && !description.trim().isEmpty()) {
            Notification.show(description,
                    Notification.Type.WARNING_MESSAGE);
        }
    }

}
