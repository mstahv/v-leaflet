package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.marker.Marker;
import org.discotools.gwt.leaflet.client.marker.MarkerOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LeafletMarker.class)
public class LeafletMarkerConnector extends AbstractComponentConnector {
	
	MarkerServerRpc rpc = RpcProxy
            .create(MarkerServerRpc.class, this);

	private Marker marker;

	@Override
	public Label getWidget() {
		// use label as "fake widget"
		return (Label) super.getWidget();
	}

	@Override
	public LeafletMarkerState getState() {
		return (LeafletMarkerState) super.getState();
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
				LatLng latlng = new LatLng(getState().point.getLat(),
						getState().point.getLon());
				Options options = new MarkerOptions();
//				options.setProperty("color", "cyan");
//				marker = new Circle(latlng, 200, options);
				// MarkerOptions options1 = new MarkerOptions();
				// Marker marker2 = new Marker(latlng, options);
				 marker = new Marker(latlng, options);
				marker.addTo(getParent().getMap());

				EventHandler<?> handler = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						rpc.onClick();
					}
				};
				
				EventHandlerManager.addEventHandler(marker, Events.click, handler);

			}
		});
	}

	@Override
	public LeafletMapConnector getParent() {
		return (LeafletMapConnector) super.getParent();
	}

}
