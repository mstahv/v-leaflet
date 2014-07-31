package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.communication.ServerRpc;

public interface MouseOutServerRpc extends ServerRpc {
	
	void onMouseOut(Point p);

}
