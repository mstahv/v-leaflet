package org.vaadin.addon.leaflet.draw.client;

import org.peimari.gleaflet.client.Circle;
import org.peimari.gleaflet.client.draw.DrawCircle;
import org.peimari.gleaflet.client.draw.DrawCircleOptions;
import org.peimari.gleaflet.client.draw.DrawFeature;
import org.peimari.gleaflet.client.draw.LayerCreatedEvent;
import org.peimari.gleaflet.client.draw.LayerCreatedListener;
import org.vaadin.addon.leaflet.client.U;
import org.vaadin.addon.leaflet.draw.LDrawCircle;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

/**
 * 
 */
@Connect(LDrawCircle.class)
public class LeafletDrawCircleConnector extends AbstractLeafletDrawFeatureConnector {

	private LeafletDrawCircleServerRcp rpc = RpcProxy.create(
			LeafletDrawCircleServerRcp.class, this);

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {

				drawFeature = instantiateDrawFeature();
				listenerRegistration = getMap()
						.addLayerCreatedListener(new LayerCreatedListener() {

							@Override
							public void onCreate(LayerCreatedEvent event) {
								Circle layer = (Circle) event.getLayer();
								rpc.circleAdded(U.toPoint(layer.getLatLng()), layer.getRadius());
								getMap().removeListener(listenerRegistration);
								listenerRegistration = null;
							}
						});

				drawFeature.enable();
			}

		});
	}

	protected DrawFeature instantiateDrawFeature() {
		return DrawCircle.create(getMap(), DrawCircleOptions.create());
	}

}
