package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

public class FlyToTest extends AbstractTest {
    
    @Override
    public String getDescription() {
        return "Fly to functionality in Leaflet 1.0.";
    }
    
    private LMap leafletMap;
    
    @Override
    public Component getTestComponent() {
        
        leafletMap = new LMap();
        leafletMap.setWidth("600px");
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2);
        final LOpenStreetMapLayer osm = new LOpenStreetMapLayer();
        leafletMap.addLayer(osm);

        Button flyTo1 = new Button("Fly to Vaadin HQ");
        flyTo1.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                leafletMap.flyTo(new Point(60.452236, 22.299839), 14.0);
            }
        });
        
        Button flyTo2 = new Button("Fly to Golden Gate");
        flyTo2.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                leafletMap.flyTo(new Point(37.816304, -122.478543), 9.0);
            }
        });

        return new VerticalLayout(leafletMap, flyTo1, flyTo2);
    }
}
