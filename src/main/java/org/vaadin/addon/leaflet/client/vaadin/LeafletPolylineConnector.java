package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.layers.vector.Polyline;
import org.discotools.gwt.leaflet.client.layers.vector.PolylineOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LeafletPolyline.class)
public class LeafletPolylineConnector extends AbstractLeafletLayerConnector<PolylineOptions> {

	private Polyline marker;

	@Override
	public LeafletPolylineState getState() {
		return (LeafletPolylineState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		// state change events are fired in random order
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				if (marker != null) {
					getParent().getMap().removeLayer(marker);
				}
				if(getState().points == null) {
					return;
				}
				
				LatLng[] latlngs = new LatLng[getState().points.length];
				for (int i = 0; i < latlngs.length; i++) {
					Point p = getState().points[i];
					latlngs[i] = new LatLng(p.getLat(), p.getLon());
				}
				PolylineOptions options = createOptions();
				
				marker = new Polyline(latlngs, options);
				
				marker.addTo(getParent().getMap());

				EventHandler<?> handler = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						rpc.onClick();
					}
				};

				EventHandlerManager.addEventHandler(marker, Events.click,
						handler);

			}
		});
	}

	@Override
	protected PolylineOptions createOptions() {
		PolylineOptions polylineOptions = new PolylineOptions();
		polylineOptions.setColor(getState().color);
		polylineOptions.setFill(getState().fill);
		polylineOptions.setFillColor(getState().fillColor);
		return polylineOptions;
	}

	
}
