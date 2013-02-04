package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.LeafletMarkerState;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Prototype. This might be technically easier to implement as an extension with Leaflet, but
 * I'm trying this way to pave way for OL integration (which has vectorlayer,
 * extensions cannot have children -> needs to be componentcontainer)
 * 
 */
public class LMarker extends LeafletLayer {

	@Override
	protected LeafletMarkerState getState() {
		return (LeafletMarkerState) super.getState();
	}

	public LMarker(double lat, double lon) {
		getState().point = new Point(lat, lon);
	}

	public LMarker() {
	}
	
	public LMarker(Point point) {
		getState().point = point;
	}

	public void setPoint(Point p) {
		getState().point = p;
	}
	
	public Point getPoint() {
		return getState().point;
	}

	public void setIconSize(Point point) {
		getState().iconSize = point;
	}

	public void setIconAnchor(Point point) {
		getState().iconAnchor = point;
	}

}
