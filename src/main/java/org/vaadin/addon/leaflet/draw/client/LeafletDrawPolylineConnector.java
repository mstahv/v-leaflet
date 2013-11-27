package org.vaadin.addon.leaflet.draw.client;

import org.peimari.gleaflet.client.Polyline;
import org.peimari.gleaflet.client.draw.DrawFeature;
import org.peimari.gleaflet.client.draw.DrawPolyline;
import org.peimari.gleaflet.client.draw.DrawPolylineOptions;
import org.peimari.gleaflet.client.draw.LayerCreatedEvent;
import org.peimari.gleaflet.client.draw.LayerCreatedListener;
import org.vaadin.addon.leaflet.client.U;
import org.vaadin.addon.leaflet.draw.LDrawPolyline;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(LDrawPolyline.class)
public class LeafletDrawPolylineConnector extends AbstractLeafletDrawFeatureConnector {

	private LeafletDrawPolylineServerRcp rpc = RpcProxy.create(
			LeafletDrawPolylineServerRcp.class, this);
	
	@Override
	protected void extend(final ServerConnector target) {
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {


			@Override
			public void execute() {
				drawFeature = instantiateDrawFeature();
				listenerRegistration = getMap().addLayerCreatedListener(new LayerCreatedListener() {
					
					@Override
					public void onCreate(LayerCreatedEvent event) {
						Polyline layer = (Polyline) event.getLayer();
						rpc.polylineAdded(U.toPointArray(layer.getLatLngs()));
						getMap().removeListener(listenerRegistration);
						listenerRegistration = null;
					}
				});

				drawFeature.enable();
			}

		});
	}
	
	protected DrawFeature instantiateDrawFeature() {
		return DrawPolyline.create(getMap(), DrawPolylineOptions.create());
	}

}
