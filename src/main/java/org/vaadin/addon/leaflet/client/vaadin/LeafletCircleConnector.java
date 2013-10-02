package org.vaadin.addon.leaflet.client.vaadin;

import org.peimari.gleaflet.client.Circle;
import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.PathOptions;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LCircle.class)
public class LeafletCircleConnector extends
        AbstractLeafletLayerConnector<PathOptions> {

    private Circle marker;

    @Override
    public LeafletCircleState getState() {
        return (LeafletCircleState) super.getState();
    }

    @Override
    protected PathOptions createOptions() {
        PathOptions pathOptions = PathOptions.create();
        pathOptions.setColor(getState().color);
        return pathOptions;
    }

    @Override
    protected void update() {
        if (marker != null) {
            removeLayerFromParent();
            marker.removeClickListener();
        }
        LatLng latlng = LatLng.create(getState().point.getLat(),
                getState().point.getLon());
        PathOptions options = createOptions();
        marker = Circle.create(latlng, getState().radius, options);
        addToParent(marker);
        
        marker.addClickListener(new ClickListener() {
			@Override
			public void onClick(MouseEvent event) {
				LatLng latLng2 = event.getLatLng();
				Point p = new Point(latLng2.getLatitude(), latLng2.getLongitude());
				rpc.onClick(p);
			}
		});

    }

    @Override
    public ILayer getLayer() {
        return marker;
    }

}
