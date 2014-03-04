package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;

import com.vaadin.ui.Component;

public class MarkerClusterTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "Add marker test case";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
	    
		leafletMap = new LMap();

		LMarkerClusterGroup mcg = new LMarkerClusterGroup();
	    
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);
		leafletMap.setMaxZoom(19);
		
		LTileLayer pk = new LTileLayer();
		pk.setUrl("http://{s}.kartat.kapsi.fi/peruskartta/{z}/{x}/{y}.png");
		pk.setAttributionString("Maanmittauslaitos, hosted by kartat.kapsi.fi");
		pk.setMaxZoom(18);
		pk.setSubDomains("tile2");
		pk.setDetectRetina(true);
		leafletMap.addBaseLayer(pk, "Peruskartta");


	    
		mcg.addComponent(new LMarker(60.4525, 22.301));
		mcg.addComponent(new LMarker(60.4526, 22.302));
		mcg.addComponent(new LMarker(60.4527, 22.303));
		mcg.addComponent(new LMarker(60.4528, 22.304));
		leafletMap.addComponent(mcg);

		return leafletMap;

	}

	@Override
	protected void setup() {
		super.setup();


	}
}
