package org.vaadin.addon.leaflet.draw.client;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;

public interface LeafletDrawPolylineServerRcp extends ServerRpc {

	public void polylineAdded(Point[] pointArray);

}
