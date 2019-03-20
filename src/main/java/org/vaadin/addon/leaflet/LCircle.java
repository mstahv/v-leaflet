package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.LeafletCircleState;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import org.locationtech.jts.geom.Geometry;

/**
 * A class for drawing circle overlays on a map.
 */
public class LCircle extends AbstractLeafletVector {

    public LCircle() {
    }

    @Override
    protected LeafletCircleState getState() {
        return (LeafletCircleState) super.getState();
    }

    @Override
    protected LeafletCircleState getState(boolean markAsDirty) {
        return (LeafletCircleState) super.getState(markAsDirty);
    }

    /**
     *
     * @param lat the latitude of the center of the circle
     * @param lon the longitude of the center of the circle
     * @param radius the radius in meters, note that this is approximate,
     * especially small zoom levels.
     */
    public LCircle(double lat, double lon, double radius) {
        getState().point = new Point(lat, lon);
        setRadius(radius);
    }

    /**
     * 
     * @param point the center of the circle
     * @param radius the radius in meters, note that this is approximate,
     * especially small zoom levels.
     */
    public LCircle(Point point, double radius) {
        setPoint(point);
        setRadius(radius);
    }

    /**
     * 
     * @param jtsPoint the center of the circle
     * @param radius the radius in meters, note that this is approximate,
     * especially small zoom levels.
     */
    public LCircle(org.locationtech.jts.geom.Point jtsPoint, double radius) {
        this(JTSUtil.toLeafletPoint(jtsPoint), radius);
    }

    public void setPoint(Point point) {
        getState().point = point;
    }

    /**
     * 
     * @param radius the radius in meters, note that this is approximate,
     * especially small zoom levels.
     */
    public void setRadius(double radius) {
        getState().radius = radius;
    }

    public Point getPoint() {
        return getState(false).point;
    }

    public double getRadius() {
        return getState(false).radius;
    }

    @Override
    public Geometry getGeometry() {
        return JTSUtil.toPoint(getPoint());
    }

    public void setPointAndRadiusWithoutRepaint(double radius, Point latLng) {
        getState(false).radius = radius;
        getState(false).point = latLng;
    }

}
