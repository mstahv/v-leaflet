package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.LeafletMarkerState;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Prototype. This might be technically easier to implement as an extension with Leaflet, but
 * I'm trying this way to pave way for OL integration (which has vectorlayer,
 * extensions cannot have children -> needs to be componentcontainer)
 * 
 */
public class LeafletMarker extends LeafletLayer {

	@Override
	protected LeafletMarkerState getState() {
		return (LeafletMarkerState) super.getState();
	}

	public LeafletMarker(double lat, double lon) {
		getState().point = new Point(lat, lon);
	}

}
