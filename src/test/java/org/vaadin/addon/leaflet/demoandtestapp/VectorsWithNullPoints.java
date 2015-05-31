package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addonhelpers.AbstractTest;

import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Point;

public class VectorsWithNullPoints extends AbstractTest {

    @Override
    public String getDescription() {
        return "A test to helper method to sanitize geometries (removing null points)";
    }

    private LMap leafletMap;


    public Component getTestComponent() {
        leafletMap = new LMap();

        LPolygon polygon = new LPolygon(new Point(0,0), null,new Point(1,1),new Point(2,3),new Point(0, 0));
        polygon.sanitizeGeometry();
        LPolyline polyline = new LPolyline();
        LPolyline polylineWithNullPoint = new LPolyline(new Point(0,0), null,new Point(1,1),new Point(2,3));
        polylineWithNullPoint.sanitizeGeometry();
        
        LMarker m = new LMarker(0,0);

        leafletMap.setZoomLevel(0);

        leafletMap.addComponents(polygon, polyline,polylineWithNullPoint, m);

        leafletMap.zoomToContent();

        return leafletMap;
    }
}
