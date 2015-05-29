package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addonhelpers.AbstractTest;

public class RemoveMarker extends AbstractTest {
    
    @Override
    public String getDescription() {
        return "Test removing marker.";
    }
    
    private LMap leafletMap;
    
    @Override
    public Component getTestComponent() {
        
        leafletMap = new LMap();
        leafletMap.setWidth("300px");
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2);
        
        final LMarker m = new LMarker(5, 5);
        
        leafletMap.addComponent(m);
        
        Button remove = new Button("Remove marker");
        remove.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                leafletMap.removeComponent(m);
                event.getButton().setEnabled(false);
            }
        });
        
        return new VerticalLayout(leafletMap, remove);
    }
}
