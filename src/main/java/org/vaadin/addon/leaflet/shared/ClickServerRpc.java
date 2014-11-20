package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.communication.ServerRpc;

public interface ClickServerRpc extends ServerRpc {
	
	void onClick(Point p);

}
