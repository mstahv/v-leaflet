package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class ReadOnlyTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "A visual test for the readOnly feature.";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
		leafletMap = new LMap();
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(10);
		LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
		leafletMap.addBaseLayer(layer, "OSM");
		leafletMap.addControl(new LZoom());
		leafletMap.setReadOnly(true);
		
		Button getStates = new Button("getStates", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				StringBuilder sb = new StringBuilder("\nisDraggingEnabled() = ").append(leafletMap.isDraggingEnabled())
						.append("\nisBooxZoomEnabled() = ").append(leafletMap.isBoxZoomEnabled());
				Notification.show("States", sb.toString(), Type.HUMANIZED_MESSAGE);
			}
		});
		Button toggleReadOnly = new Button("toggle readonly", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				leafletMap.setReadOnly(!leafletMap.isReadOnly());
			}
		});
		VerticalLayout verticalLayout = new VerticalLayout(leafletMap, new HorizontalLayout(getStates, toggleReadOnly));
		verticalLayout.setSizeFull();
		verticalLayout.setExpandRatio(leafletMap, 1);
		return verticalLayout;
	}
}
