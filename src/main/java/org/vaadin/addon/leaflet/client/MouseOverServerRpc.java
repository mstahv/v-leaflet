package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.communication.ServerRpc;

public interface MouseOverServerRpc extends ServerRpc {
	
	void onMouseOver(Point p);

}
