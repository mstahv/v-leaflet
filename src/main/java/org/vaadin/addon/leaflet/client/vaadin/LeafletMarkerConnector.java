package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.marker.Marker;
import org.discotools.gwt.leaflet.client.marker.MarkerOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LeafletMarker.class)
public class LeafletMarkerConnector extends AbstractLeafletLayerConnector<MarkerOptions> {

	private Marker marker;

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
				update();

			}

		});
	}
	
	@Override
	protected void update() {
		if (marker != null) {
			getParent().getMap().removeLayer(marker);
		}
		LatLng latlng = new LatLng(getState().point.getLat(),
				getState().point.getLon());
		Options options = createOptions();
		
		marker = new Marker(latlng, options);
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

	@Override
	protected MarkerOptions createOptions() {
		MarkerOptions markerOptions = new MarkerOptions();
		return markerOptions;
	}
	
	@Override
	public ILayer getLayer() {
		return marker;
	}


}
