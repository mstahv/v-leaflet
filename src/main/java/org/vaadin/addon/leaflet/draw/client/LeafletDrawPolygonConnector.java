package org.vaadin.addon.leaflet.draw.client;

import org.peimari.gleaflet.client.draw.DrawFeature;
import org.peimari.gleaflet.client.draw.DrawPolygon;
import org.peimari.gleaflet.client.draw.DrawPolygonOptions;
import org.vaadin.addon.leaflet.draw.LDrawPolygon;

import com.vaadin.shared.ui.Connect;

@Connect(LDrawPolygon.class)
public class LeafletDrawPolygonConnector extends LeafletDrawPolylineConnector {
	
	@Override
	protected DrawFeature instantiateDrawFeature() {
		return DrawPolygon.create(getMap(), DrawPolygonOptions.create());
	}

}
