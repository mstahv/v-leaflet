package org.vaadin.addon.leaflet.demoandtestapp;


import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addonhelpers.AbstractTest;

public class ZoomToExtendPointOnly extends AbstractTest {

    @Override
    public String getDescription() {
        return "A should not show empty map";
    }


	@Override
	public Component getTestComponent() {

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();

        // Getting my map.
        LMap map = new LMap();
        map.addComponent(new LOpenStreetMapLayer());
        LMarker lMarker = new LMarker(61, 22);
        map.addComponent(lMarker);
        
        map.zoomToContent();

        layout.addComponent(map);
        return layout;
	}
}
