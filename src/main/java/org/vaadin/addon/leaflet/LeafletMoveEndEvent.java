package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Bounds;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;

public class LeafletMoveEndEvent extends ConnectorEvent {

	private Bounds bounds;

	public LeafletMoveEndEvent(ClientConnector source, Bounds bounds) {
		super(source);
		this.bounds = bounds;
	}
	
	public Bounds getBounds() {
		return bounds;
	}
	
}