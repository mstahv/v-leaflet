package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.control.LAttribution;
import org.vaadin.addon.leaflet.control.LScale;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.ControlPosition;

import com.vaadin.ui.Component;

public class ControlTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "Test controls";
	}

	private LMap leafletMap;
	private LScale scale = new LScale();

	@Override
	public Component getTestComponent() {
		leafletMap = new LMap();
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);

		LTileLayer baselayer = new LTileLayer();
		baselayer.setAttributionString("&copy;OpenStreetMap contributors");
		// Note, this url should only be used for testing purposes. If you wish
		// to use cloudmade base maps, get your own API key.
		baselayer
				.setUrl("http://{s}.tile.cloudmade.com/a751804431c2443ab399100902c651e8/997/256/{z}/{x}/{y}.png");

		/*
		 * Layers control is automatically added to map if names are given when
		 * adding layers.
		 */
		leafletMap.addBaseLayer(baselayer, "CloudMade");
		/*
		 * Using nameless api, doesn't add layers control
		 */
		// leafletMap.addLayer(baselayer);

		/*
		 * Layers control can also be removed manually
		 */
		// leafletMap.getLayersControl().remove();

		scale.setPosition(ControlPosition.topright);
		scale.setImperial(false);
		scale.setMetric(true);
		leafletMap.addControl(scale);

		/*
		 * DEFAULT CONTROLS These are on there by default, but can be customized
		 * and disabled if needed.
		 */

		LAttribution attribution = new LAttribution();
		attribution.setPrefix("Leaflet with Java in JVM");
		attribution.setPosition(ControlPosition.bottomleft);
		// attribution.setEnabled(false);
		leafletMap.addControl(attribution);

		LZoom zoom = new LZoom();
		zoom.setPosition(ControlPosition.bottomright);
		// zoom.setEnabled(false);
		leafletMap.addControl(zoom);

		return leafletMap;
	}

	@Override
	protected void setup() {
		super.setup();

	}
}
