package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.Polygon;
import org.peimari.gleaflet.client.PolylineOptions;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.core.client.JsArray;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LPolygon.class)
public class LeafletPolygonConnector extends LeafletPolylineConnector {

    private Polygon marker;

    @Override
    protected void update() {
        if (marker != null) {
            removeLayerFromParent();
            marker.removeClickListener();
        }
        if (getState().points == null) {
            return;
        }

        JsArray<LatLng> latlngs = getLatLngsArray();
        PolylineOptions options = createOptions();
        marker = Polygon.create(latlngs, options);
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
}
