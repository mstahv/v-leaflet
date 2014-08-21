package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.PolylineOptions;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.LatLngBounds;
import org.peimari.gleaflet.client.MouseOutListener;
import org.peimari.gleaflet.client.MouseOverListener;
import org.peimari.gleaflet.client.Rectangle;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.EventId;

@Connect(org.vaadin.addon.leaflet.LRectangle.class)
public class LeafletRectangleConnector extends LeafletPolygonConnector {

    private Rectangle marker;

    @Override
    public LeafletRectangleState getState() {
        return (LeafletRectangleState) super.getState();
    }

    @Override
    protected void update() {
        if (marker != null) {
            removeLayerFromParent();
            marker.removeClickListener();
        }
        if (getState().bounds == null) {
            return;
        }

        LatLngBounds bounds = getLatLngBounds();
        PolylineOptions options = createOptions();
        marker = Rectangle.create(bounds, options);
        addToParent(marker);

        marker.addClickListener(new ClickListener() {
			@Override
			public void onClick(MouseEvent event) {
              rpc.onClick(new Point(event.getLatLng().getLatitude(), event
              .getLatLng().getLongitude()));
			}
		});
    }
    
    @Override
    public ILayer getLayer() {
    	return marker;
    }
    protected LatLngBounds getLatLngBounds() {
        Bounds b = getState().bounds;
        LatLngBounds latlngs = LatLngBounds.create(
                LatLng.create(b.getSouthWestLat(), b.getSouthWestLon()),
                LatLng.create(b.getNorthEastLat(), b.getNorthEastLon()));
        return latlngs;
    }
}
