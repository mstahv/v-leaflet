package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addonhelpers.AbstractTest;

public class TextOnMap extends AbstractTest {

	@Override
	public String getDescription() {
		return "Draw some text to the center of the world.";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
		
		leafletMap = new LMap();
        
        leafletMap.addLayer(new LOpenStreetMapLayer());
        
		leafletMap.setCenter(0, 0);
		leafletMap.setZoomLevel(2);
        
        LMarker m = new LMarker(0,0);
        m.setStyleName("mycustomclassname"); // <- this becomes's to div icon's class names
        m.setDivIcon("Hello <strong>world</strong>!");
        // define the size for the html box
        m.setIconSize(new Point(80,20));
        leafletMap.addLayer(m);


		return leafletMap;
	}
}
