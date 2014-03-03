package org.vaadin.addon.leaflet.markercluster.client;

import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LayerGroup;
import org.vaadin.addon.leaflet.client.AbstractLeafletLayerConnector;
import org.vaadin.addon.leaflet.client.LeafletFeatureGroupConnector;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.gleaflet.markercluster.client.MarkerClusterGroup;

import com.vaadin.client.ServerConnector;
import com.vaadin.shared.ui.Connect;

@Connect(LMarkerClusterGroup.class)
public class LeafletMarkerClusterConnector extends LeafletFeatureGroupConnector {
	
	@Override
	protected LayerGroup createLayerGroup() {
		return MarkerClusterGroup.create();
	}

	public AbstractLeafletLayerConnector<?> getConnectorFor(ILayer iLayer) {
		for (ServerConnector c : getChildren()) {
			if (c instanceof AbstractLeafletLayerConnector<?>) {
				AbstractLeafletLayerConnector<?> lc = (AbstractLeafletLayerConnector<?>) c;
				if(lc.getLayer() == iLayer) {
					return lc;
				}
			}
		}
		return null;
	}

}
