package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.communication.ClientRpc;

public interface LeafletMapClientRpc extends ClientRpc {
	
	void setCenter(Double lat, Double lon, Integer zoom);
	void zoomToExtent(Bounds bounds);
    void setMaxBounds(Bounds bounds);

}
