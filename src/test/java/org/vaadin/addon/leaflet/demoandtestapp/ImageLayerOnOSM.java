package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Notification;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
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
        ExternalResource url = new ExternalResource("https://www.lib.utexas.edu/maps/historical/newark_nj_1922.jpg");
        LImageOverlay imageOverlay = new LImageOverlay(url, new Bounds(new Point(40.712216, -74.22655),new Point(40.773941, -74.12544)));
        imageOverlay.setOpacity(0.5);
		imageOverlay.setInteractive(true);	//set true to emit mouse events
        imageOverlay.setAttribution("University of Texas");
        imageOverlay.addClickListener(new LeafletClickListener() {
			@Override
			public void onClick(LeafletClickEvent event) {
				Notification.show("Image overlay clicked!");
			}
		});
        imageOverlay.addContextMenuListener(new LeafletContextMenuListener() {
			@Override
			public void onContextMenu(LeafletContextMenuEvent event) {
				Notification.show("Image overlay context clicked!");
			}
		});
        imageOverlay.addMouseOverListener(new LeafletMouseOverListener() {
			@Override
			public void onMouseOver(LeafletMouseOverEvent event) {
				Notification.show("Mouse over image overlay!");
			}
		});
        imageOverlay.addMouseOutListener(new LeafletMouseOutListener() {
			@Override
			public void onMouseOut(LeafletMouseOutEvent event) {
				Notification.show("Mouse out of image overlay");
			}
		});
        leafletMap.addLayer(imageOverlay);
        
        leafletMap.zoomToContent();

		return leafletMap;
	}
}
