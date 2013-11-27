package org.vaadin.addon.leaflet.draw;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.client.LeafletDrawPolylineServerRcp;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Extension to initiate drawing of a Polygon on a map.
 * 
 */
public class LDrawPolygon extends AbstracLDrawFeature {

	public LDrawPolygon(LMap map) {
		super(map);
		registerRpc();
	}

	public LDrawPolygon() {
		registerRpc();
	}

	protected void registerRpc() {
		registerRpc(new LeafletDrawPolylineServerRcp() {

			@Override
			public void polylineAdded(Point[] pointArray) {
				LPolygon pl = new LPolygon(pointArray);
				fireEvent(new FeatureDrawnEvent(LDrawPolygon.this, pl));
				remove();
			}
		});
	}

}
