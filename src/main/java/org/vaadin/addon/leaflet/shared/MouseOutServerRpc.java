package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.communication.ServerRpc;

public interface MouseOutServerRpc extends ServerRpc {
	
	void onMouseOut(Point p);

}
