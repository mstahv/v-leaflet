package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.communication.ClientRpc;

public interface LeafletMapClientRpc extends ClientRpc {
	
	void setCenter(Double lat, Double lon, Double zoom);
    void flyTo(Double lat, Double lon, Double zoom);
	void zoomToExtent(Bounds bounds);
    void setMaxBounds(Bounds bounds);

}
