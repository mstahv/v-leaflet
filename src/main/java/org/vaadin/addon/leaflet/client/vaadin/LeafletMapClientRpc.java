package org.vaadin.addon.leaflet.client.vaadin;

import org.vaadin.addon.leaflet.shared.Bounds;

import com.vaadin.shared.communication.ClientRpc;

public interface LeafletMapClientRpc extends ClientRpc {
	
	void setCenter(Double lat, Double lon, Integer zoom);
	void zoomToExtent(Bounds bounds);

}
