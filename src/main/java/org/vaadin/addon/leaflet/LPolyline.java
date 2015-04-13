package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.LeafletPolylineState;
import org.vaadin.addon.leaflet.jsonmodels.PointArray;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

import java.util.Arrays;
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

    public LPolyline(com.vividsolutions.jts.geom.LineString jtsLineString) {
        this(JTSUtil.toLeafletPointArray(jtsLineString));
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().geometryjson = points.asJson();
    }

    public void setPoints(Point... array) {
        points = new PointArray(Arrays.asList(array));
        markAsDirty();
    }

    public void setPoints(List<Point>  points) {
        this.points = new PointArray(points);
        markAsDirty();
    }

    public Point[] getPoints() {
        return points.toArray(new Point[points.size()]);
    }

    @Override
    public Geometry getGeometry() {
        return JTSUtil.toLineString(this);
    }

    public void setGeometry(LineString lineString) {
        setPoints(JTSUtil.toLeafletPointArray(lineString));
    }

}
