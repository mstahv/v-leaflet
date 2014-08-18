package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;

public class LeafletMouseOutEvent extends ConnectorEvent {
	private Point point;

	public LeafletMouseOutEvent(ClientConnector source, Point p) {
		super(source);
		this.point = p;
	}
	
	public Point getPoint() {
		return point;
	}
	
}