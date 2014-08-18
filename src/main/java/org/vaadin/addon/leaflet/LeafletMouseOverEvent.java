package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;

public class LeafletMouseOverEvent extends ConnectorEvent {
	private Point point;

	public LeafletMouseOverEvent(ClientConnector source, Point p) {
		super(source);
		this.point = p;
	}
	
	public Point getPoint() {
		return point;
	}
	
}