package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;

public class LeafletMoveEndEvent extends ConnectorEvent {

	private Bounds bounds;
	private int zoomLevel;
        private Point center;

	public LeafletMoveEndEvent(ClientConnector source, Bounds bounds, Point center, int zoomlevel) {
		super(source);
		this.bounds = bounds;
                this.center = center;
		this.zoomLevel = zoomlevel;
	}
	
	public Bounds getBounds() {
		return bounds;
	}
	
	public Point getCenter() {
            return center;
        }
	
	public int getZoomLevel() {
		return zoomLevel;
	}
	
}