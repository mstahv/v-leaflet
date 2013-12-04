package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;


public class LFeatureGroup extends LLayerGroup {

    public void addMultiPolygon(MultiPolygon mp) {
        for (int i = 0; i < mp.getNumGeometries(); i++) {
            //Should always cast correctly as it's definitely a MultiPolygon instance
            Polygon polygon = (Polygon) mp.getGeometryN(i);
            addPolygon(polygon);
        }
    }

    public void addPolygon(Polygon polygon) {
        Coordinate[] coords = polygon.getBoundary().getCoordinates();
        Point[] points = new Point[coords.length];
        for (int i = 0; i < coords.length; i++) {
            points[i] = new Point(coords[i].y, coords[i].x);
            LPolygon lPolygon = new LPolygon(points);
            addComponent(lPolygon);
        }
    }

    public void addLineString(LineString ls) {

        Coordinate[] coords = ls.getCoordinates();
        Point[] points = new Point[coords.length];
        for (int i = 0; i < coords.length; i++) {
            points[i] = new Point(coords[i].y, coords[i].x);
            LPolyline lPolyline = new LPolyline(points);
            addComponent(lPolyline);
        }
        
    }

}
