package org.vaadin.addon.leaflet.client.vaadin;

import org.peimari.gleaflet.client.Circle;
import org.peimari.gleaflet.client.EditableMap;
import org.peimari.gleaflet.client.FeatureGroup;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.LatLngBounds;
import org.peimari.gleaflet.client.Marker;
import org.peimari.gleaflet.client.Polygon;
import org.peimari.gleaflet.client.Polyline;
import org.peimari.gleaflet.client.Rectangle;
import org.peimari.gleaflet.client.draw.Draw;
import org.peimari.gleaflet.client.draw.DrawControlOptions;
import org.peimari.gleaflet.client.draw.LayerCreatedEvent;
import org.peimari.gleaflet.client.draw.LayerCreatedListener;
import org.peimari.gleaflet.client.draw.LayerType;
import org.peimari.gleaflet.client.resources.LeafletDrawResourceInjector;
import org.vaadin.addon.leaflet.draw.LDraw;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(LDraw.class)
public class LeafletDrawConnector extends AbstractControlConnector<Draw> {
	static {
		LeafletDrawResourceInjector.ensureInjected();
	}

	private LeafletDrawServerRcp rpc = RpcProxy.create(
			LeafletDrawServerRcp.class, this);

	@Override
	protected Draw createControl() {
		DrawControlOptions options = DrawControlOptions.create();
		LeafletFeatureGroupConnector fgc = (LeafletFeatureGroupConnector) getState().featureGroup;
		FeatureGroup layerGroup = (FeatureGroup) fgc.getLayer();
		options.setEditableFeatureGroup(layerGroup);
		Draw l = Draw.create(options);

		getMap().addLayerCreatedListener(new LayerCreatedListener() {

			@Override
			public void onCreated(LayerCreatedEvent event) {
				LayerType type = event.getLayerType();
				/* type specific actions... */
				switch (type) {
				case marker:
					Marker m = (Marker) event.getLayer();
					rpc.markerDrawn(U.toPoint(m.getLatLng()));
					return;
				case circle:
					Circle c = (Circle) event.getLayer();
					rpc.circleDrawn(U.toPoint(c.getLatLng()), c.getRadius());
					break;
				case polygon:
				case rectangle:
					Polygon p = (Polygon) event.getLayer();
					rpc.polygonDrawn(U.toPointArray(p.getLatLngs()));
					break;
				case polyline:
					Polyline pl = (Polyline) event.getLayer();
					rpc.polylineDrawn(U.toPointArray(pl.getLatLngs()));
					break;
				default:
					break;
				}
			}
		});
		return l;
	}

	protected void doStateChange(StateChangeEvent stateChangeEvent) {

	}

	@Override
	protected EditableMap getMap() {
		return super.getMap().cast();
	}

	@Override
	public LeafletDrawState getState() {
		return (LeafletDrawState) super.getState();
	}

}
