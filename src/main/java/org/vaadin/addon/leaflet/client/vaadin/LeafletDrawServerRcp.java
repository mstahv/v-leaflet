package org.vaadin.addon.leaflet.client.vaadin;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.communication.ServerRpc;

public interface LeafletDrawServerRcp extends ServerRpc {
	
	public void markerDrawn(Point p);

	public void circleDrawn(Point point, double radius);

	public void polygonDrawn(Point[] latLngs);

	public void polylineDrawn(Point[] latLngs);

}
