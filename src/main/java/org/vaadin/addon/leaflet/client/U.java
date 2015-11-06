package org.vaadin.addon.leaflet.client;

import com.vaadin.client.VConsole;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.LatLngBounds;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

public class U {

    public static Point toPoint(org.peimari.gleaflet.client.Point point) {
        VConsole.log("Point " + point.getX() + " " + point.getY());
        return new Point(point.getY(), point.getX());
    }
    
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

    public static Bounds toBounds(LatLngBounds b) {
        return new Bounds(
                new Point(b.getSouthWest().getLatitude(), b.getSouthWest().getLongitude()),
                new Point(b.getNorthEast().getLatitude(), b.getNorthEast().getLongitude()));
    }
}
