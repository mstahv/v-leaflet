package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.AbstractLeafletLayer;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.control.LScale;
import org.vaadin.addon.leaflet.control.LZoom;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import org.vaadin.addonhelpers.AbstractTest;

public class MapInWindowIssue19 extends AbstractTest {

	@Override
	public String getDescription() {
		return "Run this test with ?debug and close subwindow, errors should not be thrown.";
	}

	@Override
	public Component getTestComponent() {
		 Button button = new Button("Click Me");
	        button.addClickListener(new Button.ClickListener() {
	            public void buttonClick(ClickEvent event) {
	            	Window window = new Window();
	            	LMap map = createMap();
	            	map.setSizeFull();
	            	window.setContent(map);
	            	window.setModal(true);
	            	window.setWidth("90%");
	            	window.setHeight("90%");
	            	addWindow(window);
	            }
	        });

	        return button;
	}
	
	private LMap createMap() {
		final LMap map = new LMap();
		map.setCenter(41.920833176630296, 1.853337480434182);
		map.setZoomLevel(5);
 
		map.addControl(new LScale());
		map.addControl(new LZoom());
 
		AbstractLeafletLayer[] layers = new AbstractLeafletLayer[] {
			createTileLayer(
				"OSM",
				"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
				"a", "b", "c")
		};
		addLayers(map, layers);
 
		return map;
	}
 
	private static void addLayers(LMap map, AbstractLeafletLayer... layers) {
		for (int i = 0; i < layers.length; i++) {
			if (i == 0) {
				layers[i].setActive(true);
			} else {
				layers[i].setActive(false);
			}
			map.addBaseLayer(layers[i], layers[i].getCaption());
		}
	}
 
	private AbstractLeafletLayer createTileLayer(String name, String url, String... servers) {
		return createTileLayer(name, url, false, servers);
	}
 
	private AbstractLeafletLayer createTileLayer(String name, String url, boolean tms,
			String... servers) {
		LTileLayer layer = new LTileLayer();
		layer.setCaption(name);
		layer.setUrl(url);
		layer.setActive(false);
		layer.setTms(tms);
		layer.setSubDomains(servers);
		return layer;
	}
	
}
