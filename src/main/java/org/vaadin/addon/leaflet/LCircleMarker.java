package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;


/**
 * A circle of a fixed size with radius specified in pixels.
 */
public class LCircleMarker extends LCircle {

	public LCircleMarker(double lat, double lon, double radiusInPixels) {
		super(lat, lon, radiusInPixels);
	}

	public LCircleMarker(Point point, double radiusInPixels) {
		super(point, radiusInPixels);
	}
	
	public LCircleMarker(org.locationtech.jts.geom.Point jtsPoint, double radiusInPixels) {
		super(jtsPoint, radiusInPixels);
	}

    public LCircleMarker() {
        super();
    }
}
