package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.util.Arrays;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LRectangle;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.draw.LDraw;

public class RectangleTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test for Rectangle.";
    }
    protected LDraw draw;

    public void enableDrawing() {
        if (null == draw) {
            draw = new LDraw();
            draw.setEditableFeatureGroup(featureGroup);

            leafletMap.addControl(draw);
            draw.addFeatureDrawnListener(new LDraw.FeatureDrawnListener() {
                @Override
                public void featureDrawn(LDraw.FeatureDrawnEvent event) {
                    LeafletLayer feature = event.getDrawnFeature();
                    featureGroup.addComponent(feature);
                    Notification.show("Drawn " + feature.getClass().getSimpleName());
                }
            });

            draw.addFeatureModifiedListener(new LDraw.FeatureModifiedListener() {
                @Override
                public void featureModified(LDraw.FeatureModifiedEvent event) {
                    LeafletLayer feature = event.getModifiedFeature();
                    Notification.show("Modified " + feature.getClass().getSimpleName());
                    if (feature instanceof LPolyline) {
                        LPolyline pl = (LPolyline) feature;
                        Point[] points = pl.getPoints();
                        System.out.println(Arrays.toString(points));
                    }
                }
            });

            draw.addFeatureDeletedListener(new LDraw.FeatureDeletedListener() {
                @Override
                public void featureDeleted(LDraw.FeatureDeletedEvent event) {
                    LeafletLayer feature = event.getDeletedFeature();
                    featureGroup.removeComponent(feature);
                    Notification.show("Deleted " + feature.getClass().getSimpleName());
                }
            });
        }
    }

    private LMap leafletMap;
    private LFeatureGroup featureGroup;

    @Override
    public Component getTestComponent() {
        leafletMap = new LMap();
        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        leafletMap.addBaseLayer(layer, "OSM");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(0);

        LRectangle rectangle = new LRectangle(new Point(0, 360), new Point(60, 280));
        featureGroup = new LFeatureGroup();
        featureGroup.addComponent(rectangle);
        leafletMap.addComponent(featureGroup);

        enableDrawing();
        return leafletMap;
    }

}
