package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.LatLng;
import org.vaadin.addon.leaflet.shared.Point;

public class U {
	public static Point toPoint(LatLng latLng) {
		return new Point(latLng.getLatitude(), latLng.getLongitude());
	}

	public static Point[] toPointArray(LatLng[] latLngs) {
		Point[] r = new Point[latLngs.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = toPoint(latLngs[i]);
		}
		return r;
	}
}
