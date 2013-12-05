package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;

import com.vividsolutions.jts.geom.LinearRing;

public class LPolygon extends LPolyline {

    public LPolygon(Point... points) {
        super(points);
    }
    
    public LPolygon(LinearRing jtsLinearRing) {
    	super(jtsLinearRing);
    }
    
}
