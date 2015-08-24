package org.vaadin.addon.leaflet.shared;

import java.io.Serializable;

public class Bounds implements Serializable {

    private double southWestLng;
    private double southWestLat;
    private double northEastLng;
    private double northEastLat;

    public Bounds() {
        setSouthWestLat(Double.MAX_VALUE);
        setSouthWestLon(Double.MAX_VALUE);
        setNorthEastLat(Double.NEGATIVE_INFINITY);
        setNorthEastLon(Double.NEGATIVE_INFINITY);
    }

    public Bounds(String bounds) {
        String[] split = bounds.split(",");
        setSouthWestLon(Double.parseDouble(split[0]));
        setSouthWestLat(Double.parseDouble(split[1]));
        setNorthEastLon(Double.parseDouble(split[2]));
        setNorthEastLat(Double.parseDouble(split[3]));
    }

    public Bounds(Point... points) {
        this();
        extend(points);
    }

    public double getSouthWestLon() {
        return southWestLng;
    }

    public void setSouthWestLon(double southWestLng) {
        this.southWestLng = southWestLng;
    }

    public double getSouthWestLat() {
        return southWestLat;
    }

    public void setSouthWestLat(double southWestLat) {
        this.southWestLat = southWestLat;
    }

    public double getNorthEastLon() {
        return northEastLng;
    }

    public void setNorthEastLon(double northEastLng) {
        this.northEastLng = northEastLng;
    }

    public double getNorthEastLat() {
        return northEastLat;
    }

    public void setNorthEastLat(double northEastLat) {
        this.northEastLat = northEastLat;
    }

    public void extend(Point... points) {
        for (Point point : points) {
            extend(point.getLat(), point.getLon());
        }
    }

    public void extend(double latitude, double longitude) {
        setNorthEastLat(Math.max(getNorthEastLat(), latitude));
        setNorthEastLon(Math.max(getNorthEastLon(), longitude));
        setSouthWestLat(Math.min(getSouthWestLat(), latitude));
        setSouthWestLon(Math.min(getSouthWestLon(), longitude));
    }

    public Point getCenter() {
        return new Point((getNorthEastLat() + getSouthWestLat()) / 2,
                (getNorthEastLon() + getSouthWestLon()) / 2);
    }

    @Override
    public String toString() {
        return "Bounds{" + "southWestLng=" + southWestLng + ", southWestLat=" + southWestLat + ", northEastLng=" + northEastLng + ", northEastLat=" + northEastLat + '}';
    }

}
