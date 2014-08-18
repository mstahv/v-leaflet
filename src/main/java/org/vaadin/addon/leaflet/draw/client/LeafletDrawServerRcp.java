package org.vaadin.addon.leaflet.draw.client;

import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.shared.Bounds;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;

public interface LeafletDrawServerRcp extends ServerRpc {
	
	public void markerDrawn(Point p);

	public void circleDrawn(Point point, double radius);

	public void polygonDrawn(Point[] latLngs);

        public void rectangleDrawn(Bounds bounds);

	public void polylineDrawn(Point[] latLngs);

	public void layerDeleted(Connector c);
	
	public void markerModified(Connector mc, Point newPoint);

	public void circleModified(Connector cc, Point latLng,
			double radius);

	public void polylineModified(Connector plc,
			Point[] pointArray);

	public void rectangleModified(Connector rc, Bounds bounds);
}
