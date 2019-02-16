package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;

import org.vaadin.addon.leaflet.shared.LeafletRectangleState;
import org.vaadin.addon.leaflet.shared.Bounds;

public class LRectangle extends LPolygon {

    public LRectangle(Point sw, Point ne) {
        this(new Bounds(sw, ne));
    }

    public LRectangle(Bounds b) {
        super();
        setBounds(b);
    }

    public void setBounds(Bounds bounds) {
        getState().bounds = bounds;
        setPoints(
                new Point(bounds.getSouthWestLat(), bounds.getSouthWestLon()),
                new Point(bounds.getNorthEastLat(), bounds.getSouthWestLon()),
                new Point(bounds.getNorthEastLat(), bounds.getNorthEastLon()),
                new Point(bounds.getSouthWestLat(), bounds.getNorthEastLon()));
    }

    public Bounds getBounds() {
        return getState().bounds;
    }
    
    @Override
    protected LeafletRectangleState getState() {
        return (LeafletRectangleState) super.getState();
    }
}
