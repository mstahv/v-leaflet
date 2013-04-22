package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class DynamicChanges extends AbstractTest {

    @Override
    public String getDescription() {
        return "A simple test";
    }

    LeafletClickListener listener = new LeafletClickListener() {

        @Override
        public void onClick(LeafletClickEvent event) {
            if (event.getPoint() != null) {
                Notification.show(String.format("Clicked %s @ %.4f,%.4f", event
                        .getConnector().getClass().getSimpleName(), event
                        .getPoint().getLat(), event.getPoint().getLon()));

            } else {
                Notification.show(String.format("Clicked %s", event
                        .getConnector().getClass().getSimpleName()));
            }
        }
    };
    
    private LMap leafletMap;
    private LPolyline pl = new LPolyline(new Point(60.44, 22.30),
            new Point(60.456, 22.304));

    @Override
    public Component getTestComponent() {
        leafletMap = new LMap();
        
        BaseLayer baselayer = new BaseLayer();
        baselayer.setName("CloudMade");

        // Note, this url should only be used for testing purposes. If you wish
        // to use cloudmade base maps, get your own API key.
        baselayer
                .setUrl("http://{s}.tile.cloudmade.com/a751804431c2443ab399100902c651e8/997/256/{z}/{x}/{y}.png");
        leafletMap.setBaseLayers(baselayer);
        
        leafletMap.addComponent(pl);
        
        leafletMap.zoomToExtent(new Bounds(pl.getPoints()));

        return leafletMap;
    }
    
    Random r = new Random(1);

    @Override
    protected void setup() {
        super.setup();
        
        HorizontalLayout tools = new HorizontalLayout();


        Button button = new Button("Random new point");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	ArrayList<Point> arrayList = new ArrayList<Point>(Arrays.asList(pl.getPoints()));
            	arrayList.add(new Point(60 + r.nextInt(10), 20 + r.nextInt(10)));
            	pl.setPoints(arrayList.toArray(new Point[0]));
            }
        });
        tools.addComponent(button);

        button = new Button("Zoom to polyline");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	leafletMap.zoomToExtent(new Bounds(pl.getPoints()));
            }
        });
        tools.addComponent(button);
        
        button = new Button("Center to last point ");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	Point[] points = pl.getPoints();
            	leafletMap.setCenter(points[points.length -1]);
            }
        });
        tools.addComponent(button);

        button = new Button("Zoom to last point");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	Point[] points = pl.getPoints();
				leafletMap.zoomToExtent(new Bounds(points[points.length -1]));
            }
        });
        tools.addComponent(button);
        
        button = new Button("Show current zoom");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	Integer zoomLevel = leafletMap.getZoomLevel();
            	Notification.show("Zoomlevel is " + zoomLevel);
            }
        });
        tools.addComponent(button);

        
        button = new Button("Show current center point");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	Notification.show("Center point is " + leafletMap.getCenter());
            }
        });
        tools.addComponent(button);


        content.addComponentAsFirst(tools);

    }
}
