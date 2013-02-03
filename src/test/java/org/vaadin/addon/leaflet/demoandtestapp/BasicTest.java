package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
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
			if (event.getPoint() != null) {
				Notification.show(String.format("Clicked %s @ %.4f,%.4f", event
						.getConnector().getClass().getSimpleName(), event
						.getPoint().getLat(), event.getPoint().getLon()));
			} else {
				Notification.show(String.format("Clicked %s", event
						.getConnector().getClass().getSimpleName()));
			}
		}
	};

	@Override
	public Component getTestComponent() {
		LMap leafletMap = new LMap();
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);

		leafletMap.setControls(Control.values());

		LPolyline leafletPolyline = new LPolyline(new Point(60.45, 22.295),
				new Point(60.4555, 22.301), new Point(60.45, 22.307));
		leafletPolyline.setColor("#FF00FF");
		leafletPolyline.setFill(true);
		leafletPolyline.setFillColor("#00FF00");
		leafletPolyline.addClickListener(listener);
		leafletMap.addComponent(leafletPolyline);

		LCircle leafletCircle = new LCircle(60.4525, 22.301, 300);
		leafletCircle.setColor("#00FFFF");
		leafletCircle.addClickListener(listener);
		leafletMap.addComponent(leafletCircle);

		LMarker leafletMarker = new LMarker(60.4525, 22.301);
		leafletMarker.addClickListener(listener);
		leafletMap.addComponent(leafletMarker);

		leafletMarker = new LMarker(60.4525, 22.301);
		leafletMarker.setIcon(new ClassResource("testicon.png"));
		leafletMarker.setIconSize(new Point(57, 52));
		leafletMarker.setIconAnchor(new Point(57, 26));
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
		// baselayer.setDetectRetina(true);

		BaseLayer pk = new BaseLayer();
		pk.setName("Peruskartta");
		pk.setUrl("http://{s}.kartat.kapsi.fi/peruskartta/{z}/{x}/{y}.png");
		pk.setAttributionString("Maanmittauslaitos, hosted by kartat.kapsi.fi");
		pk.setMaxZoom(18);
		pk.setSubDomains("tile2");
		pk.setDetectRetina(true);

		leafletMap.setBaseLayers(baselayer, pk);

		leafletMap.addClickListener(listener);

		leafletMap.addMoveEndListener(new LeafletMoveEndListener() {
			@Override
			public void onMoveEnd(LeafletMoveEndEvent event) {
				Bounds b = event.getBounds();
				Notification.show(
						String.format("New viewport (%.4f,%.4f ; %.4f,%.4f)",
								b.getSouthWestLat(), b.getSouthWestLon(),
								b.getNorthEastLat(), b.getNorthEastLon()),
						Type.TRAY_NOTIFICATION);
			}
		});

		return leafletMap;
	}

}
