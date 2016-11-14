package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.annotations.Push;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.peimari.gleaflet.client.CircleMarker;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

import java.util.Date;

public class GeolocationAPIExample extends AbstractTest {

    @Override
    public String getDescription() {
        return "Testing geolocation features.";
    }

    private LMap leafletMap;

    @Override
    public Component getTestComponent() {
        // setPollInterval(1000);

        leafletMap = new LMap();
        leafletMap.setWidth("600px");
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2);
        final LOpenStreetMapLayer osm = new LOpenStreetMapLayer();
        leafletMap.addLayer(osm);


        final Label pos = new Label();

        final LMarker m = new LMarker();
        m.setPopup("That's you");
        final LCircleMarker cm = new LCircleMarker();
        final LCircle c = new LCircle();

        leafletMap.addLocateListener(new LeafletLocateListener() {
            @Override
            public void onLocate(LeafletLocateEvent event) {
                pos.setValue(new Date().toString() + ": " + event.toString());
                if(m.getParent() == null) {
                    m.setPoint(event.getPoint());
                    cm.setPoint(event.getPoint());
                    cm.setColor("red");
                    cm.setRadius(1);
                    c.setPoint(event.getPoint());
                    c.setColor("yellow");
                    c.setStroke(false);
                    c.setRadius(event.getAccuracy());
                    leafletMap.addComponents(m, cm, c);
                    leafletMap.setLayersToUpdateOnLocate(m, cm, c);
                }
            }
        });

        Button locate = new Button("Locate");
        locate.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                leafletMap.locate();
            }
        });

        Button watch = new Button("Watch location");
        watch.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                leafletMap.locate(true, true, true);
            }
        });

        Button stop = new Button("Stop");
        stop.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                leafletMap.stopLocate();
            }
        });

        return new VerticalLayout(leafletMap, locate, watch, stop, pos);
    }
}
