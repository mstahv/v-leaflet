package org.vaadin.addon.leaflet.draw;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.client.vaadin.LeafletDrawPolylineServerRcp;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Extension to initiate drawing of a Polyline on a map.
 * 
 */
public class LDrawPolyline extends AbstracLDrawFeature {

	public LDrawPolyline(LMap map) {
		super(map);
		registerRpc();
	}

	public LDrawPolyline() {
		registerRpc();
	}

	protected void registerRpc() {
		registerRpc(new LeafletDrawPolylineServerRcp() {

			@Override
			public void polylineAdded(Point[] pointArray) {
				LPolyline pl = new LPolyline(pointArray);
				fireEvent(new FeatureDrawnEvent(LDrawPolyline.this, pl));
				remove();
			}
		});
	}

}
