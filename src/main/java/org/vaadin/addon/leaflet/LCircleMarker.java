package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;


public class LCircleMarker extends LCircle {

	public LCircleMarker(double lat, double lon, double radius) {
		super(lat, lon, radius);
	}

	public LCircleMarker(Point point, double radius) {
		super(point, radius);
	}

}
