package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;

import com.vaadin.ui.Component;

public class SimpleMarkerTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "Add marker test case";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
	    

		leafletMap = new LMap();

		LTileLayer pk = new LTileLayer();
		pk.setUrl("http://{s}.kartat.kapsi.fi/peruskartta/{z}/{x}/{y}.png");
		pk.setAttributionString("Maanmittauslaitos, hosted by kartat.kapsi.fi");
		pk.setMaxZoom(18);
		pk.setSubDomains("tile2");
		pk.setDetectRetina(true);
		leafletMap.addBaseLayer(pk, "Peruskartta");

		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);
		leafletMap.addComponent(new LMarker(60.4525, 22.301));
	    
		return leafletMap;

	}

	@Override
	protected void setup() {
		super.setup();


	}
}
