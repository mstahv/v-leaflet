package org.vaadin.addon.leaflet.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.*;
import org.vaadin.addon.leaflet.shared.LeafletTooltipState;
import org.vaadin.addon.leaflet.shared.TooltipState;

@Connect(org.vaadin.addon.leaflet.LTooltip.class)
public class LeafletTooltipConnector extends AbstractComponentConnector {

    static private Label fakeWidget = new Label();

    private Tooltip tooltip;

    private Map map;

    public LeafletTooltipConnector() {
    }

    @Override
    public LeafletTooltipState getState() {
        return (LeafletTooltipState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (tooltip != null) {
                    getMap().closeTooltip(tooltip);
                }
                LatLng latlng = LatLng.create(getState().point.getLat(), getState().point.getLon());
                TooltipOptions options = tooltipOptionsFor(getState().tooltipState, LeafletTooltipConnector.this);

                tooltip = (Tooltip) Tooltip.create(options).setLatLng(latlng);
                tooltip.setContent(getState().htmlContent);
                tooltip.addTo(getMap());
            }
        });
    }

    private Map getMap() {
        if (map == null) {
            ServerConnector parent = getParent();
            while (parent != null) {
                if (parent instanceof LeafletMapConnector) {
                    map = ((LeafletMapConnector) parent).getMap();
                }
                parent = parent.getParent();
            }
        }
        return map;
    }

    public static TooltipOptions tooltipOptionsFor(TooltipState tooltipState, AbstractComponentConnector c) {
        if (tooltipState == null) {
            return null;
        }
        TooltipOptions tooltipOptions = TooltipOptions.create();

        if (tooltipState.pane != null) {
            tooltipOptions.setPane(tooltipState.pane);
        }
        if (tooltipState.offset != null) {
            tooltipOptions.setOffset(Point.create(tooltipState.offset.getLat(), tooltipState.offset.getLon()));
        }
        if (tooltipState.direction != null) {
            tooltipOptions.setDirection(tooltipState.direction);
        }
        if (tooltipState.permanent != null) {
            tooltipOptions.setPermanent(tooltipState.permanent);
        }
        if (tooltipState.sticky != null) {
            tooltipOptions.setSticky(tooltipState.sticky);
        }
        if (tooltipState.interactive != null) {
            tooltipOptions.setInteractive(tooltipState.interactive);
        }
        if (tooltipState.opacity != null) {
            tooltipOptions.setOpacity(tooltipState.opacity);
        }
        String stylename = StyleUtil.getStyleNameFromComponentState(c.getState());
        tooltipOptions.setClassName(stylename);
        return tooltipOptions;
    }

    @Override
    public Label getWidget() {
        // use label as "fake widget"
        return fakeWidget;
    }

    @Override
    public void onUnregister() {
        if (tooltip != null) {
            try {
                getMap().closeTooltip(tooltip);
            } catch (Exception e) {
                VConsole.log("Tooltip already closed? " + e.getMessage());
            }
        }
        super.onUnregister();
    }
}
