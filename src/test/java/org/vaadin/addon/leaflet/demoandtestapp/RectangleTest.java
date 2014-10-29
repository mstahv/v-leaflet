package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LRectangle;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addonhelpers.AbstractTest;

public class RectangleTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test for Rectangle.";
    }

    @Override
    public Component getTestComponent() {
        LMap leafletMap = new LMap();

        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        leafletMap.addBaseLayer(layer, "OSM");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(0);

        LPolyline rectangle = new LRectangle(new Bounds(new Point(0, 360),new Point(60, 280)));
        leafletMap.addComponent(rectangle);
        return leafletMap;
    }

}
