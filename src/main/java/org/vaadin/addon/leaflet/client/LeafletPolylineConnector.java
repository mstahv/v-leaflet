package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.Polyline;
import org.peimari.gleaflet.client.PolylineOptions;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.JsArrayObject;
import com.vaadin.shared.ui.Connect;

import org.peimari.gleaflet.client.MouseOutListener;
import org.peimari.gleaflet.client.MouseOverListener;
import org.vaadin.addon.leaflet.shared.EventId;

@Connect(org.vaadin.addon.leaflet.LPolyline.class)
public class LeafletPolylineConnector extends
		AbstractLeafletVectorConnector<LeafletPolylineState, PolylineOptions> {

	private Polyline marker;

	@Override
	protected void update() {
		if (marker != null) {
			removeLayerFromParent();
			marker.removeClickListener();
                        marker.removeMouseOverListener();
                        marker.removeMouseOutListener();
		}
		if (getState().points == null) {
			return;
		}

		PolylineOptions options = createOptions();
		marker = Polyline.create(getLatLngsArray(), options);

		marker.addClickListener(new ClickListener() {

			@Override
			public void onClick(MouseEvent event) {
				rpc.onClick(new Point(event.getLatLng().getLatitude(), event
						.getLatLng().getLongitude()));
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

		addToParent(marker);
	}

	@Override
	public ILayer getLayer() {
		return marker;
	}

	protected JsArray<LatLng> getLatLngsArray() {
		JsArray<LatLng> latlngs = JsArrayObject.createArray().cast();
		for (Point p : getState().points) {
			latlngs.push(LatLng.create(p.getLat(), p.getLon()));
		}
		return latlngs;
	}

}
