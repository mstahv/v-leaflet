package org.vaadin.addon.leaflet;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;
import org.vaadin.addon.leaflet.shared.Point;

public class LeafletLocateEvent extends ConnectorEvent {
	private final Double accuracy;
	private final Double altitude;
	private Point point;

	public LeafletLocateEvent(ClientConnector source, Point p, Double a, Double altitude) {
		super(source);
		this.point = p;
		this.accuracy = a;
		this.altitude = altitude;
	}
	
	public Point getPoint() {
		return point;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public Double getAltitude() {
		return altitude;
	}

	@Override
	public String toString() {
		return point.toString() + " a: " + accuracy + " alt:" + altitude;
	}
}