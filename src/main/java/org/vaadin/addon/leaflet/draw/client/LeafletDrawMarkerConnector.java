package org.vaadin.addon.leaflet.draw.client;

import org.peimari.gleaflet.client.Marker;
import org.peimari.gleaflet.client.draw.DrawFeature;
import org.peimari.gleaflet.client.draw.DrawMarker;
import org.peimari.gleaflet.client.draw.DrawMarkerOptions;
import org.peimari.gleaflet.client.draw.LayerCreatedEvent;
import org.peimari.gleaflet.client.draw.LayerCreatedListener;
import org.vaadin.addon.leaflet.client.U;
import org.vaadin.addon.leaflet.draw.LDrawMarker;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

/**
 * 
 */
@Connect(LDrawMarker.class)
public class LeafletDrawMarkerConnector extends AbstractLeafletDrawFeatureConnector {

	private LeafletDrawMarkerServerRcp rpc = RpcProxy.create(
			LeafletDrawMarkerServerRcp.class, this);

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
								Marker layer = (Marker) event.getLayer();
								rpc.markerAdded(U.toPoint(layer.getLatLng()));
								getMap().removeListener(listenerRegistration);
								listenerRegistration = null;
							}
						});

				drawFeature.enable();
			}

		});
	}

	protected DrawFeature instantiateDrawFeature() {
		return DrawMarker.create(getMap(), DrawMarkerOptions.create());
	}

}
