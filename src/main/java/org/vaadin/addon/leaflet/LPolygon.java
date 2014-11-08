package org.vaadin.addon.leaflet;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import org.vaadin.addon.leaflet.client.LeafletPolylineState;
import org.vaadin.addon.leaflet.jsonmodels.PointArray;
import org.vaadin.addon.leaflet.jsonmodels.PointMultiArray;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import java.util.Arrays;
import java.util.List;

public class LPolygon extends AbstractLeafletVector {

    private PointMultiArray points = new PointMultiArray();

    @Override
    protected LeafletPolylineState getState() {
        return (LeafletPolylineState) super.getState();
    }

    public LPolygon(Point... points) {
        setPoints(points);
    }

    public LPolygon(LinearRing jtsLinearRing) {
        this(JTSUtil.toLeafletPointArray(jtsLinearRing));
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().geometryjson = points.asJson();
    }

    public void setPoints(Point... array) {
        PointArray outerbounds = new PointArray(Arrays.asList(array));
        if(points.size() == 0) {
            points.add(outerbounds);
        } else {
            points.set(0, outerbounds);
        }
        markAsDirty();
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

}
