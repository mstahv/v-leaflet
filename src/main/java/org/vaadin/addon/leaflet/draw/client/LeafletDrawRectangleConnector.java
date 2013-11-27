package org.vaadin.addon.leaflet.draw.client;

import org.peimari.gleaflet.client.draw.DrawFeature;
import org.peimari.gleaflet.client.draw.DrawRectangle;
import org.peimari.gleaflet.client.draw.DrawRectangleOptions;
import org.vaadin.addon.leaflet.draw.LDrawRectangle;

import com.vaadin.shared.ui.Connect;

@Connect(LDrawRectangle.class)
public class LeafletDrawRectangleConnector extends LeafletDrawPolylineConnector {
	
	@Override
	protected DrawFeature instantiateDrawFeature() {
		return DrawRectangle.create(getMap(), DrawRectangleOptions.create());
	}

}
