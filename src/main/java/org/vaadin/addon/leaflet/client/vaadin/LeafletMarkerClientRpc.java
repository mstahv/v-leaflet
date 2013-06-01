package org.vaadin.addon.leaflet.client.vaadin;

import com.vaadin.shared.communication.ClientRpc;

public interface LeafletMarkerClientRpc extends ClientRpc {
	
	public void openPopup();
	public void closePopup();

}
