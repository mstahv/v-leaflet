package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window;

public class SizeChangeInWindow extends AbstractTest {

    @Override
    public String getDescription() {
        return "Window resize should notify leaflet about size change.";
    }

    @Override
    public Component getTestComponent() {
    	
    	final LMap map = new LMap();
    	     map.setCenter( 50.06465, 19.94498 );
    	
    	      final BaseLayer baselayer = new BaseLayer();
    	      baselayer.setName( "OpenStreetMap" );
    	      baselayer.setAttributionString( "OpenStreetMap" );
    	      baselayer.setUrl( "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" );
    	      map.setBaseLayers( baselayer );
    	  
    	      map.setImmediate( true );
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
