package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.LeafletCircleState;
import org.peimari.gleaflet.client.Circle;
import org.peimari.gleaflet.client.CircleOptions;
import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.MouseEvent;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.shared.ui.Connect;

import org.peimari.gleaflet.client.MouseOutListener;
import org.peimari.gleaflet.client.MouseOverListener;
import org.vaadin.addon.leaflet.shared.EventId;

@Connect(org.vaadin.addon.leaflet.LCircle.class)
public class LeafletCircleConnector extends
        AbstractLeafletVectorConnector<LeafletCircleState, CircleOptions> {

    private Circle marker;

    @Override
    protected void update() {
        if (marker != null) {
            removeLayerFromParent();
            marker.removeClickListener();
            marker.removeMouseOverListener();
            marker.removeMouseOutListener();
        }
        LatLng latlng = LatLng.create(getState().point.getLat(),
                getState().point.getLon());
        CircleOptions options = createOptions();
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
        if (hasEventListener(EventId.MOUSEOVER)) {
			/*
			 * Add listener lazily to avoid extra event if layer is modified in
			 * server side listener. This can be removed if "clear and rebuild"
			 * style component updates are changed into something more
			 * intelligent at some point.
			 */
        	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
		            marker.addMouseOverListener(new MouseOverListener() {
		                @Override
		                public void onMouseOver(MouseEvent event) {
		                    mouseOverRpc.onMouseOver(U.toPoint(event.getLatLng()));
		                }
		            });
				}
        	});
        }
        if (hasEventListener(EventId.MOUSEOUT)) {
            marker.addMouseOutListener(new MouseOutListener() {
                @Override
                public void onMouseOut(MouseEvent event) {
                    mouseOutRpc.onMouseOut(U.toPoint(event.getLatLng()));
                }
            });
        }
    }

    @Override
    public ILayer getLayer() {
        return marker;
    }
    
}
