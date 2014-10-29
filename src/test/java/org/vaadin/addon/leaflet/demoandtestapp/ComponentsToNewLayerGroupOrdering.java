package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addonhelpers.AbstractTest;

public class ComponentsToNewLayerGroupOrdering extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test layer ordering";
    }

    @Override
    public Component getTestComponent() {
        final MapView view1 = new MapView();
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        Button button1 = new Button("Red");
        Button button2 = new Button("Blue");
        layout.addComponent(view1);
        button1.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                view1.redUp();
                System.out.println("Red Up");
            }
        });
        button2.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                view1.redDown();
                System.out.println("Red Down");

            }
        });
        layout.addComponent(button1);
        layout.addComponent(button2);
        layout.addComponent(view1);

        return layout;
    }

    public class MapView extends LMap {
    	
    	LCircleMarker[] reds = new LCircleMarker[3];
    	LCircleMarker[] blues = new LCircleMarker[3];
    	
        public MapView() {
            setHeight("300px");
            setWidth("300px");
            
            reds[0] = m("red",32.3, 34.8);
            reds[1] = m("red",32.2, 34.6);
            reds[2] = m("red",32.27, 34.58);
            
            blues[0] = m("blue",32.34, 34.0);
            blues[1] = m("blue",32.25, 34.62);
            blues[2] = m("blue",32.29, 34.5);
            
            redUp();
            zoomToContent();
        }
        
        LCircleMarker m(String color, double lat, double lon) {
            LCircleMarker marker = new LCircleMarker(lat,lon, 30);
            marker.setFillColor(color);
            marker.getStyle().setFillOpacity(1.0);
            return marker;
        }

        public void redUp() {
            removeAllComponents();
            addComponents(new LLayerGroup(blues),new LLayerGroup(reds));
        }

        public void redDown() {
            removeAllComponents();
            addComponents(new LLayerGroup(reds),new LLayerGroup(blues));
        }

    }

}
