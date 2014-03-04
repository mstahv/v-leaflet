package org.vaadin.addon.leaflet.markercluster.client;

import org.peimari.gleaflet.client.LayerGroup;
import org.vaadin.addon.leaflet.client.LeafletFeatureGroupConnector;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.gleaflet.markercluster.client.MarkerClusterGroup;

import com.vaadin.shared.ui.Connect;

@Connect(LMarkerClusterGroup.class)
public class LeafletMarkerClusterConnector extends LeafletFeatureGroupConnector {
	
	@Override
	protected LayerGroup createLayerGroup() {
		return MarkerClusterGroup.create();
	}

}
