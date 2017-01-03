package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

public class LayerGroupOrdering extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test layer ordering (currently broken)";
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

        LLayerGroup blues;
        LLayerGroup reds;

        public MapView() {
            setHeight("300px");
            setWidth("300px");

            Point center = new Point(32.32, 34.85);
            setCenter(center);
            setZoomLevel(9);

            blues = new LLayerGroup();
            addComponent(blues);
            reds = new LLayerGroup();
            addComponent(reds);

            LCircleMarker marker = new LCircleMarker(32.3, 34.8, 30);
            marker.setFillColor("red");
            marker.getStyle().setFillOpacity(1.0);
            blues.addComponent(marker);

            marker = new LCircleMarker(32.34, 34.0, 30);
            marker.setFillColor("blue");
            marker.getStyle().setFillOpacity(1.0);
            reds.addComponent(marker);

            marker = new LCircleMarker(32.2, 34.6, 30);
            marker.setFillColor("red");
            marker.getStyle().setFillOpacity(1.0);
            blues.addComponent(marker);

            marker = new LCircleMarker(32.25, 34.62, 30);
            marker.setFillColor("blue");
            marker.getStyle().setFillOpacity(1.0);
            reds.addComponent(marker);
            marker = new LCircleMarker(32.27, 34.58, 30);
            marker.setFillColor("red");
            marker.getStyle().setFillOpacity(1.0);
            blues.addComponent(marker);

            marker = new LCircleMarker(32.29, 34.5, 30);
            marker.setFillColor("blue");

            marker.getStyle().setFillOpacity(1.0);
            reds.addComponent(marker);
        }

        public void redUp() {
            removeAllComponents();
            addComponent(reds);
            addComponent(blues);
        }

        public void redDown() {
            removeAllComponents();
            addComponent(blues);
            addComponent(reds);
        }

    }

}
