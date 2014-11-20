package org.vaadin.addon.leaflet.shared;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.communication.ServerRpc;

public interface DragEndServerRpc extends ServerRpc {
	
	void dragEnd(Point point);
	
}
