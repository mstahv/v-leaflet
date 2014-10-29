package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.draw.AbstracLDrawFeature;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnListener;
import org.vaadin.addon.leaflet.draw.LDrawCircle;
import org.vaadin.addon.leaflet.draw.LDrawMarker;
import org.vaadin.addon.leaflet.draw.LDrawPolygon;
import org.vaadin.addon.leaflet.draw.LDrawPolyline;
import org.vaadin.addon.leaflet.draw.LDrawRectangle;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.OptionGroup;
import org.vaadin.addonhelpers.AbstractTest;

public class DrawWithoutToolbarTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "Test leaflet draw";
	}

	private LMap leafletMap;
	private LLayerGroup group;

	@Override
	public Component getTestComponent() {

		leafletMap = new LMap();
		leafletMap.setCenter(0, 0);
		leafletMap.setZoomLevel(0);
		leafletMap.addLayer(new LTileLayer(
				"http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"));

		group = new LFeatureGroup();

		leafletMap.addLayer(group);

		return leafletMap;
	}

	@Override
	protected void setup() {
		super.setup();

		final OptionGroup optionGroup = new OptionGroup();
		optionGroup.addItem(LDrawPolyline.class);
		optionGroup.addItem(LDrawPolygon.class);
		optionGroup.addItem(LDrawRectangle.class);
		optionGroup.addItem(LDrawMarker.class);
		optionGroup.addItem(LDrawCircle.class);
		content.addComponent(optionGroup);
		
		Button button = new Button("Draw", new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Class<? extends AbstracLDrawFeature> value = (Class<? extends AbstracLDrawFeature>) optionGroup.getValue();
				try {
					AbstracLDrawFeature newInstance = value.newInstance();
					newInstance.addTo(leafletMap);
					newInstance.addFeatureDrawnListener(new FeatureDrawnListener() {
						
						@Override
						public void featureDrawn(FeatureDrawnEvent event) {
							group.addComponent(event.getDrawnFeature());
						}
					});
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		content.addComponent(button);

	}
}
