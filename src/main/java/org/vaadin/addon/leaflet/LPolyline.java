package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.LeafletPolylineState;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vividsolutions.jts.geom.Geometry;

public class LPolyline extends AbstractLeafletVector {

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

    public void setPoints(Point... array) {
        getState().points = array;
    }
 
	public Point[] getPoints() {
		return getState().points;
	}

	@Override
	public Geometry getGeometry() {
		return JTSUtil.toLineString(this);
	}

}
