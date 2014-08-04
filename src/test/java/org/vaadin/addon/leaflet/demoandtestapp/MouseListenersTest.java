package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LeafletMouseOutEvent;
import org.vaadin.addon.leaflet.LeafletMouseOutListener;
import org.vaadin.addon.leaflet.LeafletMouseOverEvent;
import org.vaadin.addon.leaflet.LeafletMouseOverListener;

public class MouseListenersTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test for mouse{Over|Out} events.<br/>Polygon should be red on hovering.";
    }

    @Override
    public Component getTestComponent() {
        LMap leafletMap = new LMap();

        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        leafletMap.addBaseLayer(layer, "OSM");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(0);

        final LPolyline lPolyline = new LPolygon(new Point(0, 360), new Point(0, 390), new Point(60, 370));
        leafletMap.addComponent(lPolyline);
        lPolyline.addMouseOverListener(new LeafletMouseOverListener() {
            @Override
            public void onMouseOver(LeafletMouseOverEvent event) {
                lPolyline.setColor("red");
            }
        });
        lPolyline.addMouseOutListener(new LeafletMouseOutListener() {
            @Override
            public void onMouseOut(LeafletMouseOutEvent event) {
                lPolyline.setColor("blue");
            }
        });

        return leafletMap;
    }

}
