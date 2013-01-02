package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.layers.vector.Circle;
import org.discotools.gwt.leaflet.client.marker.MarkerOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LeafletCircle.class)
public class LeafletCircleConnector extends AbstractLeafletLayerConnector {

	private Circle marker;

	@Override
	public LeafletCircleState getState() {
		return (LeafletCircleState) super.getState();
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
				// FIXME hard coded color
				options.setProperty("color", "cyan");
				marker = new Circle(latlng, 200, options);
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

}
