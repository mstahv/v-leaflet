package org.vaadin.addon.vodatime.demoandtestapp;

import org.vaadin.addon.leaflet.LeafletLayer.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletLayer.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletCircle;
import org.vaadin.addon.leaflet.LeafletMap;
import org.vaadin.addon.leaflet.LeafletMarker;
import org.vaadin.addon.leaflet.LeafletPolyline;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;

public class BasicTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "A simple test";
	}
	
	LeafletClickListener listener = new LeafletClickListener() {
		
		@Override
		public void onClick(LeafletClickEvent event) {
			Notification.show("Clicked object (" + event.getConnector().getClass().getSimpleName() + ")");
		}
	};

	@Override
	Component getTestComponent() {
		LeafletMap leafletMap = new LeafletMap();
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);

		LeafletMarker leafletMarker = new LeafletMarker(60.4525, 22.301);
		leafletMarker.addClickListener(listener);
		leafletMap.addComponent(leafletMarker);
		
		LeafletCircle leafletCircle = new LeafletCircle(60.4525, 22.301, 300);
		leafletCircle.setColor("cyan");
		leafletCircle.addClickListener(listener);
		leafletMap.addComponent(leafletCircle);
		
		LeafletPolyline leafletPolyline = new LeafletPolyline(new Point(60.45,22.295), new Point(60.4555,22.301), new Point(60.45,22.307));
		leafletPolyline.setColor("magenta");
		leafletPolyline.setFill(true);
		leafletPolyline.setFillColor("green");
		leafletPolyline.addClickListener(listener);
		leafletMap.addComponent(leafletPolyline);


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
