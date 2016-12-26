package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addonhelpers.AbstractTest;

import java.util.concurrent.atomic.AtomicInteger;

public class MarkerZIndexOffset extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test removing marker.";
    }

    @Override
    public Component getTestComponent() {

        LMap leafletMap = new LMap();
        leafletMap.setWidth("300px");
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2);

        final AtomicInteger currentBase = new AtomicInteger(1000);

        final LMarker m = new LMarker(5, 5);
        m.setId("5 5");
        final LMarker m2 = new LMarker(9, 9);
        m2.setZIndexOffset(currentBase.get());
        m2.setId("9 9");
//        m.setZIndexOffset(currentBase.getValue());

        final LMarker m3 = new LMarker(7, 7);

        leafletMap.addComponents(m, m2, m3);

        Button move = new Button("Move top right on top");
        move.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                m.setZIndexOffset(currentBase.get());
                currentBase.addAndGet(1000);
                m2.setZIndexOffset(currentBase.get());
            }
        });

        Button move2 = new Button("Move low left to top");
        move2.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                m2.setZIndexOffset(currentBase.get());
                currentBase.addAndGet(1000);
                m.setZIndexOffset(currentBase.get());
            }
        });

        return new VerticalLayout(leafletMap, move, move2);
    }
}
