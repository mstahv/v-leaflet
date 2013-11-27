package org.vaadin.addon.leaflet.draw;

import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.client.LeafletDrawCircleServerRcp;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Extension to initiate drawing of a Circle on a map.
 */
public class LDrawCircle extends AbstracLDrawFeature {

	public LDrawCircle(LMap map) {
		super(map);
		registerRpc();
	}

	public LDrawCircle() {
		registerRpc();
	}

	protected void registerRpc() {
		registerRpc(new LeafletDrawCircleServerRcp() {
			
			@Override
			public void circleAdded(Point latLng, double radius) {
				LCircle lCircle = new LCircle(latLng, radius);
				fireEvent(new FeatureDrawnEvent(LDrawCircle.this, lCircle));
				remove();
			}
		});
	}

}
