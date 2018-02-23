package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addonhelpers.AbstractTest;

public class SplitpanelIssue170 extends AbstractTest {

    @Override
    public String getDescription() {
        return "Leaflet map in SplitPanel";
    }

    @Override
    public Component getTestComponent() {

        LMap leafletMap = new LMap();
        leafletMap.setSizeFull();
        Bounds bounds = new Bounds();
        bounds.setSouthWestLon(15.3308);
        bounds.setSouthWestLat(41.1427);
        bounds.setNorthEastLat(39.8847);
        bounds.setNorthEastLon(16.887);

        leafletMap.zoomToExtent(bounds);

        leafletMap.addLayer(new LTileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"));
        HorizontalSplitPanel sp = new HorizontalSplitPanel();
        sp.setSizeFull();
        sp.setFirstComponent(leafletMap);
        sp.setSecondComponent(new Label("My Label"));

        return sp;

    }
}
