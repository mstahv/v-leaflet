package org.vaadin.addonhelpers;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Theme("valo")
public abstract class AbstractTest extends UI {

    protected VerticalLayout content;
    public static Map<Class<? extends AbstractTest>, List<com.vaadin.server.ErrorEvent>> allErrors
            = new ConcurrentHashMap<>();

    public AbstractTest() {
        content = new VerticalLayout();
        setContent(content);
        setErrorHandler(event -> {
                    List<com.vaadin.server.ErrorEvent> errorEvents =
                            allErrors.computeIfAbsent(getClass(), clazz -> new CopyOnWriteArrayList<>());
                    errorEvents.add(event);
                }
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
     */
    public void setContentSize(Component content) {
        content.setSizeFull();
    }

    public abstract Component getTestComponent();

    @Override
    protected void init(VaadinRequest request) {
        try {
            setup();
        } catch (Throwable e) {
            getErrorHandler().error(new com.vaadin.server.ErrorEvent(e));
            throw e;
        }
        String description = getDescription();
        if (description != null && !description.trim().isEmpty()) {
            Notification.show(description,
                    Notification.Type.WARNING_MESSAGE);
        }
    }

}
