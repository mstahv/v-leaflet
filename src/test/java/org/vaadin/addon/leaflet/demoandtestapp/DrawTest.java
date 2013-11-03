package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.draw.LDraw;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;

public class DrawTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "Test leaflet draw";
	}

	private LMap leafletMap;
	private LDraw draw = new LDraw();

	@Override
	public Component getTestComponent() {
		leafletMap = new LMap();
		leafletMap.setCenter(0, 0);
		leafletMap.setZoomLevel(0);

		LFeatureGroup group = new LFeatureGroup();

		group.addComponent(new LPolyline(new Point(-100, -30),
				new Point(20, 10), new Point(40, 150)));

		leafletMap.addLayer(group);
		
		draw.setEditableFeatureGroup(group);
		
		leafletMap.addControl(draw);
		

		return leafletMap;
	}

	@Override
	protected void setup() {
		super.setup();

	}
}
