package org.vaadin.addon.leaflet;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.vaadin.addon.leaflet.shared.LeafletPolylineState;
import org.vaadin.addon.leaflet.jsonmodels.PointArray;
import org.vaadin.addon.leaflet.jsonmodels.PointMultiArray;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import java.util.Arrays;
import java.util.List;
import static org.vaadin.addon.leaflet.util.JTSUtil.toLeafletPointArray;

public class LPolygon extends AbstractLeafletVector {

    private PointMultiArray points = new PointMultiArray();

    @Override
    protected LeafletPolylineState getState() {
        return (LeafletPolylineState) super.getState();
    }

    public LPolygon(Point... points) {
        setPoints(points);
    }

    public LPolygon(Polygon polygon) {
        setGeometry(polygon);
    }

    public LPolygon setGeometry(Polygon polygon) {
        setGeometryWithoutRepaint(polygon);
        markAsDirty();
        return this;
    }

    /**
     * Sets the geometry without triggering repaint on the client side. Can be used
     * by add-ons that modify vector on the client side
     * 
     * @param polygon the geometry
     */
    public void setGeometryWithoutRepaint(Polygon polygon) {
        Point[] exterior = toLeafletPointArray(polygon.getExteriorRing());
        PointMultiArray pointMultiArray = new PointMultiArray();
        pointMultiArray.add(new PointArray(exterior));
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            pointMultiArray.add(new PointArray(toLeafletPointArray(polygon.getInteriorRingN(i))));
        }
        points = pointMultiArray;
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().geometryjson = points.asJson();
    }

    public void setPoints(Point... array) {
        PointArray outerbounds = new PointArray(Arrays.asList(array));
        if (points.size() == 0) {
            points.add(outerbounds);
        } else {
            points.set(0, outerbounds);
        }
        markAsDirty();
    }

    public LPolygon setPointsAndholes(Point[][] pointsAndHoles) {
        points = new PointMultiArray();
        for (int i = 0; i < pointsAndHoles.length; i++) {
            Point[] pa = pointsAndHoles[i];
            points.add(new PointArray(pa));
        }
        markAsDirty();
        return this;
    }

    public Point[] getPoints() {
        PointArray outerRing = points.get(0);
        return outerRing.toArray(new Point[outerRing.size()]);
    }

    @Override
    public Geometry getGeometry() {
        return JTSUtil.toPolygon(this);
    }

    public LPolygon addHole(Point... hole) {
        points.add(new PointArray(hole));
        return this;
    }

    public LPolygon setHoles(Point[]... holes) {
        for (Point[] hole : holes) {
            addHole(hole);
        }
        return this;
    }

    public List<PointArray> getHoles() {
        return points.subList(1, points.size());
    }

    /**
     * Removes all null values from the geometry.
     */
    public void sanitizeGeometry() {
        points.sanitize();
    }

}
