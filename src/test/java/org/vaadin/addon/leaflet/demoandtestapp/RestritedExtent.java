package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

public class RestritedExtent extends AbstractTest {

    @Override
    public String getDescription() {
        return "A test for using custom/explicit projection. Ensure the "
                + "WMS layer uses the projection given to map.";
    }

    @Override
    public Component getTestComponent() {
        final LMap leafletMap = new LMap();
        leafletMap.setHeight("300px");
        leafletMap.setWidth("300px");
        leafletMap.setZoomLevel(15);

        // leafletMap.addBaseLayer(new LOpenStreetMapLayer(), "OSM");

        Point p = new Point(60, 22);
        Point p2 = new Point(61, 23);
        final Bounds b = new Bounds(p, p2);
        leafletMap.setMaxBounds(b);
        leafletMap.zoomToExtent(b);

        leafletMap.addComponent(new LPolyline(p, p2));

        Button button = new Button("Move restricted extent (aka max bounds)");
        button.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                
                b.setNorthEastLat(b.getNorthEastLat() + 0.5);
                b.setNorthEastLon(b.getNorthEastLon() + 0.5);
                b.setSouthWestLat(b.getSouthWestLat() + 0.5);
                b.setSouthWestLon(b.getSouthWestLon() + 0.5);
                leafletMap.setMaxBounds(b);

                leafletMap.addComponent(new LPolyline(new Point(b.getSouthWestLat(), b.getSouthWestLon()), new Point(b.getNorthEastLat(), b.getNorthEastLon())));

                leafletMap.zoomToExtent(b);
            }
        });
        return new VerticalLayout(leafletMap, button);
    }

}
