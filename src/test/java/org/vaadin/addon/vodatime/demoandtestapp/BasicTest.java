package org.vaadin.addon.vodatime.demoandtestapp;

import org.vaadin.addon.leaflet.LeafletMap;
import org.vaadin.addon.leaflet.LeafletMarker;
import org.vaadin.addon.leaflet.LeafletMarker.MarkerClickEvent;
import org.vaadin.addon.leaflet.LeafletMarker.MarkerClickListener;
import org.vaadin.addon.leaflet.shared.BaseLayer;

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

		BaseLayer baselayer = new BaseLayer();
		baselayer.setName("CloudMade");

		// Note, this url should only be used for testing purposes. If you wish
		// to use cloudmade base maps, get your own API key.
		// FIXME get API key for v-leaflet tests current is from gwtl project
		baselayer
				.setUrl("http://{s}.tile.cloudmade.com/BC9A493B41014CAABB98F0471D759707/997/256/{z}/{x}/{y}.png");
		leafletMap.setBaseLayers(baselayer);

		return leafletMap;
	}

}
