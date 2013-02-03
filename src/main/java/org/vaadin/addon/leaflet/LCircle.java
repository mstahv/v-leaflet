package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.LeafletCircleState;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Prototype. This might be technically easier to implement as an extension with
 * Leaflet, but I'm trying this way to pave way for OL integration (which has
 * vectorlayer, extensions cannot have children -> needs to be
 * componentcontainer)
 * 
 */
public class LCircle extends AbstractLeafletVector {

	@Override
	protected LeafletCircleState getState() {
		return (LeafletCircleState) super.getState();
	}

	public LCircle(double lat, double lon, double radius) {
		getState().point = new Point(lat, lon);
		getState().radius = radius;
	}

}
