package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

public class LayerClickWithCustomCursor extends AbstractTest {

	@Override
	public String getDescription() {
		return "Example how to use custom cursor directly with CSS.";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
		
		Page.getCurrent().getStyles().add(".leaflet-grab {cursor: crosshair;}");
    
		leafletMap = new LMap();
		leafletMap.setWidth("300px");
		leafletMap.setHeight("300px");
		leafletMap.setCenter(0, 0);
		leafletMap.setZoomLevel(2);
		leafletMap.addClickListener(new LeafletClickListener() {
			@Override
			public void onClick(LeafletClickEvent event) {
				leafletMap.addLayer(new LMarker(event.getPoint()));
			}
		});

		return leafletMap;
	}
}
