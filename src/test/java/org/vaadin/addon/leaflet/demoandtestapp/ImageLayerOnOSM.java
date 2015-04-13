package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.server.ExternalResource;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LImageOverlay;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addonhelpers.AbstractTest;

public class ImageLayerOnOSM extends AbstractTest {

	@Override
	public String getDescription() {
		return "Test ImageOverlay on top of OSM.";
	}

	private LMap leafletMap = new LMap();

	@Override
	public Component getTestComponent() {
        leafletMap.addLayer(new LOpenStreetMapLayer());

        // Old map overlayed approximately over OSM map
        ExternalResource url = new ExternalResource("http://www.lib.utexas.edu/maps/historical/newark_nj_1922.jpg");
        LImageOverlay imageOverlay = new LImageOverlay(url, new Bounds(new Point(40.712216, -74.22655),new Point(40.773941, -74.12544)));
        imageOverlay.setOpacity(0.5);
        imageOverlay.setAttribution("University of Texas");
        leafletMap.addLayer(imageOverlay);
        
        leafletMap.zoomToContent();

		return leafletMap;
	}
}
