package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.data.HasValue;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class DynamicChanges extends AbstractTest {

    private Registration registration;

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
    private LPolyline polyline = new LPolyline(new Point(60.44, 22.30),
            new Point(60.456, 22.304));

    @Override
    public Component getTestComponent() {
        leafletMap = new LMap();

        leafletMap.addBaseLayer(new LOpenStreetMapLayer(), "OSM");

        leafletMap.addComponent(polyline);

        leafletMap.zoomToExtent(new Bounds(polyline.getPoints()));

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
                ArrayList<Point> arrayList = new ArrayList<Point>(Arrays.asList(polyline.getPoints()));
                arrayList.add(new Point(60 + r.nextInt(10), 20 + r.nextInt(10)));
                polyline.setPoints(arrayList.toArray(new Point[0]));
            }
        });
        tools.addComponent(button);

        button = new Button("Zoom to polyline");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                leafletMap.zoomToExtent(new Bounds(polyline.getPoints()));
            }
        });
        tools.addComponent(button);

        button = new Button("Center to last point ");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Point[] points = polyline.getPoints();
                leafletMap.setCenter(points[points.length - 1]);
            }
        });
        tools.addComponent(button);

        button = new Button("Zoom to last point");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Point[] points = polyline.getPoints();
                leafletMap.zoomToExtent(new Bounds(points[points.length - 1]));
            }
        });
        tools.addComponent(button);

        button = new Button("Show current zoom");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Double zoomLevel = leafletMap.getZoomLevel();
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


        button = new Button("Show current size of the map");
        button.addClickListener(
              (e) -> leafletMap
                    .getSize(point -> Notification.show("Map size: " + point)));
        tools.addComponent(button);

        final LeafletMoveEndListener moveEndListener = new LeafletMoveEndListener() {
            @Override
            public void onMoveEnd(LeafletMoveEndEvent event) {
                Notification.show("Moved or zoomed", Type.TRAY_NOTIFICATION);
            }
        };

        final CheckBox checkBox = new CheckBox("Toggle move listener");
        checkBox.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            @Override
            public void valueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    registration = leafletMap.addMoveEndListener(moveEndListener);
                } else if (registration != null) {
                    registration.remove();
                }

            }
        });
        tools.addComponent(checkBox);

        content.addComponentAsFirst(tools);

    }
}
