package org.vaadin.addon.leaflet.draw;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.client.vaadin.LeafletDrawMarkerServerRcp;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Extension to initiate drawing of a Marker on a map.
 * 
 */
public class LDrawMarker extends AbstracLDrawFeature {

	public LDrawMarker(LMap map) {
		super(map);
		registerRpc();
	}

	public LDrawMarker() {
		registerRpc();
	}

	protected void registerRpc() {
		registerRpc(new LeafletDrawMarkerServerRcp() {
			
			@Override
			public void markerAdded(Point latLng) {
				LMarker lMarker = new LMarker(latLng);
				fireEvent(new FeatureDrawnEvent(LDrawMarker.this, lMarker));
				remove();
			}
		});
	}

}
