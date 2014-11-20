package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.communication.ServerRpc;

public interface MouseOverServerRpc extends ServerRpc {
	
	void onMouseOver(Point p);

}
