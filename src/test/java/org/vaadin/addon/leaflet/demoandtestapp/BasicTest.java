package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletCircle;
import org.vaadin.addon.leaflet.LeafletMap;
import org.vaadin.addon.leaflet.LeafletMarker;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addon.leaflet.LeafletPolyline;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.ClassResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class BasicTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "A simple test";
	}

	LeafletClickListener listener = new LeafletClickListener() {

		@Override
		public void onClick(LeafletClickEvent event) {
			if(event.getPoint() != null) {
				Notification.show(String.format("Clicked %s @ %.4f,%.4f", event
						.getConnector().getClass().getSimpleName(), event.getPoint().getLat(),
						event.getPoint().getLon()));
			} else {
				Notification.show(String.format("Clicked %s", event
						.getConnector().getClass().getSimpleName()));
			}
		}
	};

	@Override
	Component getTestComponent() {
		LeafletMap leafletMap = new LeafletMap();
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);

		LeafletPolyline leafletPolyline = new LeafletPolyline(new Point(60.45,
				22.295), new Point(60.4555, 22.301), new Point(60.45, 22.307));
		leafletPolyline.setColor("magenta");
		leafletPolyline.setFill(true);
		leafletPolyline.setFillColor("green");
		leafletPolyline.addClickListener(listener);
		leafletMap.addComponent(leafletPolyline);

		LeafletCircle leafletCircle = new LeafletCircle(60.4525, 22.301, 300);
		leafletCircle.setColor("cyan");
		leafletCircle.addClickListener(listener);
		leafletMap.addComponent(leafletCircle);

		LeafletMarker leafletMarker = new LeafletMarker(60.4525, 22.301);
		leafletMarker.addClickListener(listener);
		leafletMap.addComponent(leafletMarker);
		
		leafletMarker = new LeafletMarker(60.4525, 22.301);
		leafletMarker.setIcon(new ClassResource("testicon.png"));
		leafletMarker.setIconSize(new Point(57,52));
		leafletMarker.setIconAnchor(new Point(57,26));
		leafletMarker.addClickListener(listener);
		leafletMap.addComponent(leafletMarker);


		BaseLayer baselayer = new BaseLayer();
		baselayer.setName("CloudMade");

		// Note, this url should only be used for testing purposes. If you wish
		// to use cloudmade base maps, get your own API key.
		baselayer
				.setUrl("http://{s}.tile.cloudmade.com/a751804431c2443ab399100902c651e8/997/256/{z}/{x}/{y}.png");

		// This will make everything sharper on "retina devices", but also text
		// quite small
		baselayer.setDetectRetina(true);
		leafletMap.setBaseLayers(baselayer);

		leafletMap.addClickListener(listener);

		leafletMap.addMoveEndListener(new LeafletMoveEndListener() {
			@Override
			public void onMoveEnd(LeafletMoveEndEvent event) {
				Notification.show(String.format(
						"New viewport (%.4f,%.4f ; %.4f,%.4f)",
						event.getSouthWestLat(), event.getSouthWestLng(),
						event.getNorthEastLat(), event.getNorthEastLng()),
						Type.TRAY_NOTIFICATION);
			}
		});

		return leafletMap;
	}

}
