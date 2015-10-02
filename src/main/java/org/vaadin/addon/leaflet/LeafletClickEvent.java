package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;

public class LeafletClickEvent extends ConnectorEvent {

    private final Point point;
    private final MouseEventDetails mouseEvent;

    public LeafletClickEvent(Component source, Point p, MouseEventDetails d) {
        super(source);
        this.point = p;
        this.mouseEvent = d;
    }

    /**
     * @return the geographical point where the event happened.
     */
    public Point getPoint() {
        return point;
    }

    public double getClientX() {
        return mouseEvent.getClientX();
    }

    public double getClientY() {
        return mouseEvent.getClientY();
    }

    public double getRelativeX() {
        return mouseEvent.getRelativeX();
    }

    public double getRelativeY() {
        return mouseEvent.getRelativeY();
    }

    public MouseEventDetails getMouseEvent() {
        return mouseEvent;
    }

    @Override
    public String toString() {
        return "LeafletClickEvent{" + "point=" + point + ", mouseEvent=" + mouseEvent + '}';
    }

}
