package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Bounds;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;

public class LeafletMoveEndEvent extends ConnectorEvent {

	private Bounds bounds;
	private int zoomLevel;

	public LeafletMoveEndEvent(ClientConnector source, Bounds bounds, int zoomlevel) {
		super(source);
		this.bounds = bounds;
		this.zoomLevel = zoomlevel;
	}
	
	public Bounds getBounds() {
		return bounds;
	}
	
	public int getZoomLevel() {
		return zoomLevel;
	}
	
}