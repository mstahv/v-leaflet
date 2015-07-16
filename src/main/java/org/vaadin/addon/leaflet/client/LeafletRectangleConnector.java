package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.LeafletRectangleState;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.*;
import org.vaadin.addon.leaflet.shared.Bounds;

@Connect(org.vaadin.addon.leaflet.LRectangle.class)
public class LeafletRectangleConnector extends LeafletPolygonConnector {

    @Override
    public LeafletRectangleState getState() {
        return (LeafletRectangleState) super.getState();
    }

    @Override
    public Layer getLayer() {
    	return marker;
    }

    @Override
    protected Polygon createVector(PolylineOptions options) {
        return Rectangle.create(getLatLngBounds(), options);
   }

    protected LatLngBounds getLatLngBounds() {
        Bounds b = getState().bounds;
        LatLngBounds latlngs = LatLngBounds.create(
                LatLng.create(b.getSouthWestLat(), b.getSouthWestLon()),
                LatLng.create(b.getNorthEastLat(), b.getNorthEastLon()));
        return latlngs;
    }
}
