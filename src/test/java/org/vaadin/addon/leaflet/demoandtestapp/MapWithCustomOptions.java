package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.control.LScale;
import org.vaadin.addonhelpers.AbstractTest;

public class MapWithCustomOptions extends AbstractTest {

    private LMap leafletMap;

    @Override
    public String getDescription() {
        return "Map Options Test";
    }

    @Override
    public Component getTestComponent() {
        leafletMap = new LMap();
        final LOpenStreetMapLayer lOpenStreetMapLayer = new LOpenStreetMapLayer();
        leafletMap.addLayer(lOpenStreetMapLayer);
        leafletMap.addControl(new LScale());
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2);

        leafletMap.setZoomSnap(2.0);
        leafletMap.setZoomDelta(0.1);

        leafletMap.setCustomInitOption("closePopupOnClick", false);

        leafletMap.setBoxZoomEnabled(false);
        leafletMap.setDoubleClickZoomEnabled(false);

        return leafletMap;
    }

}
