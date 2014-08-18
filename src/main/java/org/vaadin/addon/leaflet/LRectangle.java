package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vividsolutions.jts.geom.Geometry;

public class LRectangle extends LPolygon {

    public LRectangle(Bounds b) {
        super();
        setBounds(b);
    }

    public void setBounds(Bounds bounds) {
        setPoints(
                new Point(bounds.getSouthWestLat(), bounds.getSouthWestLon()),
                new Point(bounds.getNorthEastLat(), bounds.getSouthWestLon()),
                new Point(bounds.getNorthEastLat(), bounds.getNorthEastLon()),
                new Point(bounds.getSouthWestLat(), bounds.getNorthEastLon()));
    }

    public Bounds getBounds() {
    	return new Bounds(getPoints());
    }
    
    @Override
    public Geometry getGeometry() {
        return JTSUtil.toLinearRing(this);
    }
}
