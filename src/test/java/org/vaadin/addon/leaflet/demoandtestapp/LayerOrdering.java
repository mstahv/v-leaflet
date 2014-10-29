package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addonhelpers.AbstractTest;

public class LayerOrdering extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test layer ordering";
    }

    private LMap leafletMap;

    private HorizontalLayout buttons = new HorizontalLayout();

    @Override
    public Component getTestComponent() {

        leafletMap = new LMap();
        leafletMap.setWidth("300px");
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2);

        leafletMap.addLayer(createMarker("red", -10, -10, 40));
        leafletMap.addLayer(createMarker("green",10, -10, 40));
        leafletMap.addLayer(createMarker("blue",10, 10, 40));
        leafletMap.addLayer(createMarker("cyan",-10, 10, 40));

        return leafletMap;
    }

    private LCircleMarker createMarker(String color, int x, int y, int r) {
        final LCircleMarker lCircleMarker;
        lCircleMarker = new LCircleMarker(x, y, r);
        lCircleMarker.setFillOpacity(1.0);
        lCircleMarker.setColor(color);
        lCircleMarker.setFillColor(color);
        buttons.addComponent(new Button(color + " to top", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                lCircleMarker.bringToFront();
            }
        }));
        return lCircleMarker;
    }

    @Override
    protected void setup() {
        super.setup();
        content.addComponentAsFirst(buttons);
    }

}
