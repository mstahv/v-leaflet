package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;

public class ContinuousWorld extends AbstractTest {

	@Override
	public String getDescription() {
		return "Test for continuous world and nowrap.";
	}

	@Override
	public Component getTestComponent() {
		LMap leafletMap = new LMap();

		LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
		layer.setNoWrap(true); // default false

		leafletMap.addBaseLayer(layer, "OSM");

		leafletMap.setCenter(0, 0);
		leafletMap.setZoomLevel(0);
		
		//Should cross pacific ocean
		LPolyline lPolyline = new LPolyline(new Point(0,360),new Point(0,390));
		
		leafletMap.addComponent(lPolyline);

		return leafletMap;
	}

}
