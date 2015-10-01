package org.vaadin.addon.leaflet;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;
import org.vaadin.addon.leaflet.shared.Point;

public class LeafletContextMenuEvent extends ConnectorEvent {

    private Point point;

    public LeafletContextMenuEvent(ClientConnector source, Point p) {
        super(source);
        this.point = p;
    }

    public Point getPoint() { return point; }
}
