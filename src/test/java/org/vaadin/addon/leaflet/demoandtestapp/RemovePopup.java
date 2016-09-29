package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LPopup;
import org.vaadin.addonhelpers.AbstractTest;

public class RemovePopup extends AbstractTest {
    
    @Override
    public String getDescription() {
        return "Test removing popup.";
    }
    
    private LMap leafletMap;
    
    @Override
    public Component getTestComponent() {
        
        leafletMap = new LMap();
        leafletMap.setWidth("300px");
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2);
        
        final LPopup popup = new LPopup(5.0, 5.0);
        popup.setContent("Hello!");
        leafletMap.addComponent(popup);
        
        Button remove = new Button("Remove popup");
        remove.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                leafletMap.removeComponent(popup);
                event.getButton().setEnabled(false);
            }
        });
        
        return new VerticalLayout(leafletMap, remove);
    }
}
