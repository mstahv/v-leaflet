package org.vaadin.addon.leaflet.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.json.client.JSONParser;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.*;
import org.vaadin.addon.leaflet.shared.EventId;
import org.vaadin.addon.leaflet.shared.Point;

@Connect(org.vaadin.addon.leaflet.LPolyline.class)
public class LeafletPolylineConnector extends
		AbstractLeafletVectorConnector<LeafletPolylineState, PolylineOptions> {

	protected Polyline marker;

	@Override
	protected void update() {
		if (marker != null) {
			removeLayerFromParent();
			marker.removeClickListener();
                        marker.removeMouseOverListener();
                        marker.removeMouseOutListener();
		}
		if (getState().geometryjson == null) {
			return;
		}

		marker = createVector(createOptions());

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

	protected Polyline createVector(PolylineOptions options) {
		return Polyline.createWithArray(getCoordinatesArray(), options);
	}

	protected JsArray<JsArray> getCoordinatesArray() {
		return JSONParser.parseStrict(getState().geometryjson).isArray().getJavaScriptObject().cast();
	}

}
