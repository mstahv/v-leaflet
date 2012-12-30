package org.vaadin.addon.vodatime.demoandtestapp;

import org.vaadin.addon.leaflet.LeafletMap;
import org.vaadin.addon.leaflet.LeafletMarker;
import org.vaadin.addon.leaflet.LeafletMarker.MarkerClickEvent;
import org.vaadin.addon.leaflet.LeafletMarker.MarkerClickListener;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;

public class BasicTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "A simple test";
	}

	@Override
	Component getTestComponent() {
		LeafletMap leafletMap = new LeafletMap();
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);

		LeafletMarker leafletMarker = new LeafletMarker(60.4525, 22.301);
		leafletMap.addComponent(leafletMarker);

		leafletMarker.addMarkerClickListener(new MarkerClickListener() {
			@Override
			public void onClick(MarkerClickEvent event) {
				Notification.show("Clicked marker");
			}
		});

		return leafletMap;
	}

}
