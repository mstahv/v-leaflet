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
		mcg.addComponent(new LMarker(0,0));
		leafletMap.zoomToContent();
		leafletMap.addComponent(mcg);
	    
	    
		return leafletMap;

	}

	@Override
	protected void setup() {
		super.setup();


	}
}
