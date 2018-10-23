package org.vaadin.addon.leaflet;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;
import org.vaadin.addon.leaflet.shared.Point;

public class LeafletLocateEvent extends ConnectorEvent {
	private final Double accuracy;
	private final Double altitude;
	private final Double speed;
	private Point point;

	public LeafletLocateEvent(ClientConnector source, Point p, Double a, Double altitude, Double speed) {
		super(source);
		this.point = p;
		this.accuracy = a;
		this.altitude = altitude;
		this.speed = speed;
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

    public Double getSpeed() {
        return speed;
    }

	@Override
	public String toString() {
		return point.toString() + " a: " + accuracy + " alt:" + altitude;
	}
}