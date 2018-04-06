package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.Random;

import org.vaadin.addon.leaflet.LMap;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addonhelpers.AbstractTest;

public class SizeChangeInWindow extends AbstractTest {

    @Override
    public String getDescription() {
        return "Window resize should notify leaflet about size change.";
    }

    @Override
    public Component getTestComponent() {
    	
    	final LMap map = new LMap();
    	     map.setCenter( 50.06465, 19.94498 );
    	
    	      map.addBaseLayer(new LOpenStreetMapLayer(), "OpenStreetMap");
    	  
    	      map.setSizeFull();
    	      
    	      
    	      Window window = new Window("Map window");
    	      window.setContent(map);
    	      window.setWidth("300px");
    	      window.setHeight("300px");
    	      getUI().addWindow(window);
    	      

        return new Label("Use the window...");
    }
    
    Random r = new Random(1);

}
