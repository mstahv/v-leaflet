package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.LeafletPolylineState;
import org.vaadin.addon.leaflet.shared.Point;

public class LPolyline extends AbstractLeafletVector {

    @Override
    protected LeafletPolylineState getState() {
        return (LeafletPolylineState) super.getState();
    }

    public LPolyline(Point... points) {
        getState().points = points;
    }

    public void setFill(boolean fill) {
        getState().fill = fill;
    }

    public void setFillColor(String fillColor) {
        getState().fillColor = fillColor;
    }

    public void setPoints(Point[] array) {
        getState().points = array;
    }

	public Point[] getPoints() {
		return getState().points;
	}

}
