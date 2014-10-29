package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

public class VectorPerfotest extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test to render largish set of vectors.";
    }

    @Override
    public Component getTestComponent() {
        final LMap leafletMap = new LMap();
        leafletMap.setHeight("300px");
        leafletMap.setWidth("300px");
        leafletMap.setZoomLevel(15);

        // leafletMap.addBaseLayer(new LOpenStreetMapLayer(), "OSM");
        final Random r = new Random();
        List<Point> pa = new ArrayList<Point>();
        for (int i = 0; i < 10; i++) {
            pa.add(new Point(60 + r.nextDouble() * 10, 20 + r.nextDouble() * 10));
        }

        for (int i = 0; i < 1000; i++) {
            Collections.shuffle(pa);
            LPolyline pl = new LPolyline(pa.toArray(new Point[pa.size()]));
            leafletMap.addComponent(pl);
        }

        Button button = new Button("Move restricted extent (aka max bounds)");
        button.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
            }
        });
        
        leafletMap.zoomToContent();
        return new VerticalLayout(leafletMap, button);
    }

}
