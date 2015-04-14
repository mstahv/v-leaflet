package org.vaadin.addon.leaflet.demoandtestapp;



import com.vaadin.server.ExternalResource;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LImageOverlay;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Crs;
import org.vaadin.addonhelpers.AbstractTest;

public class PlainImage extends AbstractTest {

	@Override
	public String getDescription() {
		return "Image \"zooming and panning\"";
	}

	private LMap leafletMap = new LMap();

	@Override
	public Component getTestComponent() {
        leafletMap.setCrs(Crs.Simple);
        
        ExternalResource url = new ExternalResource("https://www.dropbox.com/s/oajfgu8onqxfo0g/photo.jpg?dl=1");
        
        // The size of this image is 3264 * 2448, scale it here to suite better
        // for default zoomlevels
        final Bounds bounds = new Bounds(new Point(0, 0), new Point(244.8,326.4));
        LImageOverlay imageOverlay = new LImageOverlay(url, bounds);
        leafletMap.addLayer(imageOverlay);

        // You can fit it directly or to another extend like here, you could also
        // use multiple images on the background
        leafletMap.setMaxBounds(new Bounds(new Point(0, 0), new Point(300,500)));
        
        // draw line from corner to corner
        leafletMap.addLayer(new LPolyline(new Point(0, 0), new Point(244.8,326.4)));
        
        leafletMap.setMaxZoom(5);

		return leafletMap;
	}
}
