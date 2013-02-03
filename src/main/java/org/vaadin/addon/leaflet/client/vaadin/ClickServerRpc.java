package org.vaadin.addon.leaflet.client.vaadin;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.communication.ServerRpc;

public interface ClickServerRpc extends ServerRpc {
	
	void onClick(Point p);

	
}
