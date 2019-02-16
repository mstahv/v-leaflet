package org.vaadin.addon.leaflet;

import org.locationtech.jts.geom.Geometry;
import org.vaadin.addon.leaflet.shared.LeafletPolylineState;
import org.vaadin.addon.leaflet.jsonmodels.PointArray;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import java.util.List;

public class LPolyline extends AbstractLeafletVector {

    private PointArray points;

    @Override
    protected LeafletPolylineState getState() {
        return (LeafletPolylineState) super.getState();
    }

    public LPolyline(Point... points) {
        setPoints(points);
    }

    public LPolyline(org.locationtech.jts.geom.LineString jtsLineString) {
        this(JTSUtil.toLeafletPointArray(jtsLineString));
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().geometryjson = points.asJson();
    }

    public void setPoints(Point... array) {
        setPointsWithoutRepaint(array);
        markAsDirty();
    }

    public void setPoints(List<Point>  points) {
        this.points = new PointArray(points);
        markAsDirty();
    }

    public void setPointsWithoutRepaint(Point...  points) {
        this.points = new PointArray(points);
    }
    
    public Point[] getPoints() {
        return points.toArray(new Point[points.size()]);
    }

    @Override
    public Geometry getGeometry() {
        final LineString line = JTSUtil.toLineString(this);
        if(line.isSimple() && line.isClosed())
        {
          return JTSUtil.toLinearRing(this);
        }
        return line;
    }

    public void setGeometry(LineString lineString) {
        setPoints(JTSUtil.toLeafletPointArray(lineString));
    }
    
    public void setGeometryWithoutRepaint(LineString lineString) {
        setPointsWithoutRepaint(JTSUtil.toLeafletPointArray(lineString));
    }

    /**
     * Removes all null values from the geometry.
     */
    public void sanitizeGeometry() {
        points.sanitize();
    }

}
