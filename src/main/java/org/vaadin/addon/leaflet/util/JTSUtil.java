package org.vaadin.addon.leaflet.util;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.peimari.gleaflet.client.Polyline;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.jsonmodels.PointArray;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Helper methods to convert between JTS geometry types and v-leaflet objects.
 * <p>
 * The CRF is expected to be WGS84 (~EPSG:4326 ~ GPS coordinates) in both
 * directions.
 */
public class JTSUtil {

    /**
     * Translates a JTS {@link Geometry} to a {@link LeafletLayer}. If given
     * geometry maps to multiple LeafletLayers they are returned in a
     * LLayerGroup.
     *
     * @param geom {@link Geometry}
     *
     * @return the layer built based on JTS {@link Geometry}
     */
    public static LeafletLayer toLayer(Geometry geom) {
        Collection<LeafletLayer> layers = toLayers(geom);
        if (layers.size() != 1) {
            LLayerGroup group = new LLayerGroup();
            for (LeafletLayer l : layers) {
                group.addComponent(l);
            }
            return group;
        } else {
            return layers.iterator().next();
        }
    }

    /**
     * Translates between a JTS {@link Geometry} and one or more
     * {@link LeafletLayer}s
     *
     * @param geom {@link Geometry}
     *
     * @return a collection of layers built from JTS {@link Geometry}
     */
    public static Collection<LeafletLayer> toLayers(Geometry geom) {

        Collection<LeafletLayer> layers = new HashSet<LeafletLayer>();

        if (geom instanceof Polygon) {
            LPolygon lPolygon = toPolygon((Polygon) geom);
            layers.add(lPolygon);
            return layers;
        } else if (geom instanceof LinearRing) {
            LPolygon lPolygon = toPolygon((LinearRing) geom);
            layers.add(lPolygon);
            return layers;
        } else if (geom instanceof LineString) {
            LPolyline lPolyline = toPolyline((LineString) geom);
            layers.add(lPolyline);
        } else if (geom instanceof MultiPolygon) {
            LLayerGroup group = new LLayerGroup();
            for (int i = 0; i < geom.getNumGeometries(); i++) {
                Polygon polygon = (Polygon) geom.getGeometryN(i);
                LPolygon lPolygon = toPolygon(polygon);
                group.addComponent(lPolygon);
            }
            layers.add(group);

        } else if (geom instanceof MultiLineString) {
            LLayerGroup group = new LLayerGroup();
            for (int i = 0; i < geom.getNumGeometries(); i++) {
                LineString lineString = (LineString) geom.getGeometryN(i);
                LPolyline lPolyline = toPolyline(lineString);
                group.addComponent(lPolyline);
            }
            layers.add(group);

        } else if (geom instanceof org.locationtech.jts.geom.Point) {

            org.locationtech.jts.geom.Point point = (org.locationtech.jts.geom.Point) geom;
            LMarker lMarker = toLMarker(point);
            layers.add(lMarker);

        } else if (geom instanceof MultiPoint) {
            LLayerGroup group = new LLayerGroup();
            for (int i = 0; i < geom.getNumGeometries(); i++) {
                org.locationtech.jts.geom.Point point = (org.locationtech.jts.geom.Point) geom
                        .getGeometryN(i);
                LMarker lMarker = toLMarker(point);
                group.addComponent(lMarker);
            }
            layers.add(group);
        }

        return layers;
    }

    /**
     * Translates between a {@link org.locationtech.jts.geom.Point} and a
     * {@link LMarker}
     *
     * @param point the point to be translated into marker
     * @return LMarker in given JTS Point
     */
    public static LMarker toLMarker(org.locationtech.jts.geom.Point point) {
        LMarker lMarker = new LMarker();
        Point lPoint = new Point();
        lPoint.setLat(point.getY());
        lPoint.setLon(point.getX());
        lMarker.setPoint(lPoint);
        return lMarker;
    }

    /**
     * Translates between a JTS {@link LineString} and a v-leaflet
     * {@link Polyline}
     *
     * @param lineString the linestring to be converted into polyline
     * @return LPolyline with points form given {@link LineString}
     */
    public static LPolyline toPolyline(LineString lineString) {

        Coordinate[] coords = lineString.getCoordinates();
        Point[] points = toPointArray(coords);
        LPolyline polyLine = new LPolyline(points);
        return polyLine;
    }

    /**
     * Translates between a JTS {@link Polygon} and a v-leaflet {@link LPolygon}
     *
     * @param polygon the JTS polygon to be translated into polygon layer
     * @return LPolygon with points form given {@link Polygon}
     */
    public static LPolygon toPolygon(Polygon polygon) {
        Point[] exterior = toLeafletPointArray(polygon.getExteriorRing());
        LPolygon lPolygon = new LPolygon(exterior);
        for(int i = 0; i< polygon.getNumInteriorRing();i++) {
            lPolygon.addHole(toLeafletPointArray(polygon.getInteriorRingN(i)));
        }
        return lPolygon;
    }

    /**
     * Translates between a JTS {@link LinearRing} and a v-leaflet
     * {@link LPolygon}
     *
     * @param linearRing the JTS linearring to be translated to polygon layer
     * @return LPolygon with points form given {@link LinearRing}
     */
    public static LPolygon toPolygon(LinearRing linearRing) {
        Coordinate[] coords = linearRing.getCoordinates();
        Point[] points = toPointArray(coords);

        LPolygon lPolygon = new LPolygon(points);
        return lPolygon;
    }

    /**
     * Translates between an array of v-leaflet {@link Point}s and JTS
     * {@link Coordinate}s
     *
     * @param coords the coordinate array to be translated into v-leaflet projects internal Point data type
     * @return point array from given JTS {@link Coordinate}s
     */
    public static Point[] toPointArray(Coordinate[] coords) {
        Point[] points = new Point[coords.length];

        for (int i = 0; i < coords.length; i++) {
            points[i] = new Point(coords[i].y, coords[i].x);
        }
        return points;
    }

    public static org.vaadin.addon.leaflet.shared.Point[] toLeafletPointArray(
            LineString path) {
        Coordinate[] coordinates = path.getCoordinates();
        org.vaadin.addon.leaflet.shared.Point[] points = new org.vaadin.addon.leaflet.shared.Point[coordinates.length];
        for (int i = 0; i < points.length; i++) {
            Coordinate coordinate = coordinates[i];
            points[i] = new org.vaadin.addon.leaflet.shared.Point(coordinate.y,
                    coordinate.x);
        }
        return points;
    }

    public static LineString toLineString(
            org.vaadin.addon.leaflet.shared.Point[] points) {
        GeometryFactory factory = getGeometryFactory();
        Coordinate[] coordinates = new Coordinate[points.length];
        for (int i = 0; i < coordinates.length; i++) {
            Point p = points[i];
            coordinates[i] = new Coordinate(p.getLon(), p.getLat());
        }
        return factory.createLineString(coordinates);
    }

    public static LineString toLineString(LPolyline polyline) {
        Point[] points = polyline.getPoints();
        if(points.length < 2) {
        	// v-leaflet supports weird lines with only one points
        	if(points.length == 1) {
            	points = new Point[] {points[0],points[0]};
        	}
        }
        return toLineString(points);
    }

    public static LinearRing toLinearRing(LPolyline polyline) {
        Point[] points = polyline.getPoints();
        if(points.length == 0) {
            return null;
        }
        return toLinearRing(points);
    }

    public static Polygon toPolygon(LPolygon polygon) {
        Point[] points = polygon.getPoints();
        if(points.length == 0) {
            return null;
        }
        List<PointArray> holes = polygon.getHoles();
        if(holes.isEmpty()) {
            return getGeometryFactory().createPolygon(toLinearRing(points));
        } else {
            LinearRing[] holesLr = new LinearRing[holes.size()];
            for (int i = 0; i < holes.size(); i++) {
                holesLr[i] = toLinearRing(holes.get(i).toArray(new Point[holes.get(i).size()]));
            }
            return getGeometryFactory().createPolygon(toLinearRing(points), holesLr);
        }
    }

    private static LinearRing toLinearRing(Point[] points) {
        boolean closed = points[0].equals(points[points.length - 1]);

        Coordinate[] coordinates = new Coordinate[points.length
                + (closed ? 0 : 1)];
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            coordinates[i] = new Coordinate(p.getLon(), p.getLat());
        }
        if (!closed) {
            coordinates[coordinates.length - 1] = coordinates[0];
        }
        return getGeometryFactory().createLinearRing(coordinates);
    }

    private static GeometryFactory getGeometryFactory() {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(),
                4326);
        return factory;
    }

    public static org.locationtech.jts.geom.Point toPoint(
            org.vaadin.addon.leaflet.shared.Point p) {
        org.locationtech.jts.geom.Point point = getGeometryFactory()
                .createPoint(new Coordinate(p.getLon(), p.getLat()));
        return point;
    }

    public static org.locationtech.jts.geom.Point toPoint(LMarker lMarker) {
        return toPoint(lMarker.getPoint());
    }

    public static org.vaadin.addon.leaflet.shared.Point toLeafletPoint(
            org.locationtech.jts.geom.Point location) {
        org.vaadin.addon.leaflet.shared.Point p = new org.vaadin.addon.leaflet.shared.Point(
                location.getY(), location.getX());
        return p;
    }

    public static Bounds getBounds(Geometry geometry) {
        Geometry envelope = geometry.getEnvelope();
        return new Bounds(toPointArray(envelope.getCoordinates()));
    }
    
    public static Bounds toBounds(Coordinate... coordinates) {
        Bounds bounds = new Bounds();
        for (Coordinate coordinate : coordinates) {
            bounds.extend(coordinate.y, coordinate.x);
        }
        return bounds;
    }
    
    /**
     * Translates between a WKT representation of a geometry and a  JTS {@link Geometry}
     * @param wellKnownText the WKT String to be converted into JTS {@link Geometry}
     * @return JTS Geometry from given WKT String
     * @throws ParseException if WKT string cannot be parsed
     */
    private static Geometry toGeometry(String wellKnownText) throws ParseException {
       WKTReader wktReader = new WKTReader(getGeometryFactory());
       return wktReader.read(wellKnownText);
    }
    
    public static LeafletLayer toLayer(String wellKnownText) throws ParseException {
       return toLayer(toGeometry(wellKnownText));
    }
}
