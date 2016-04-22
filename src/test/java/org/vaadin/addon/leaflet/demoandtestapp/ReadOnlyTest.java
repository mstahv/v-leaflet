package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class ReadOnlyTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "A visual test for the readOnly feature.";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
		leafletMap = new LMap();
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);
		LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
		leafletMap.addBaseLayer(layer, "OSM");
		leafletMap.setReadOnly(true);
		return leafletMap;
	}
}
