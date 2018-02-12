package org.vaadin.addon.leaflet;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;

public class LeafletOverlayAddEvent extends ConnectorEvent {

    private String name;

    public LeafletOverlayAddEvent(ClientConnector source, String name) {
        super(source);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
