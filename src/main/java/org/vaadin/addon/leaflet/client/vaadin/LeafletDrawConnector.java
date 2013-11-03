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
					LatLng latLng = m.getLatLng();
					rpc.markerDrawn(new Point(latLng.getLatitude(), latLng
							.getLongitude()));
					return;
				case circle:
					Circle c = (Circle) event.getLayer();
//					LatLng latLng = c.getLatLng();
					double radius = c.getRadius();
//					Window.alert("Created circle at " + latLng + " with "
//							+ radius + "m radius. {"
//							+ new JSONObject(c.toGeoJSON()).toString() + "}");
					break;
				case polygon:
					Polygon p = (Polygon) event.getLayer();
					LatLng[] latlngs = p.getLatLngs();
					Window.alert("Created polygon: " + p.getRawLatLngs());
					break;
				case polyline:
					Polyline pl = (Polyline) event.getLayer();
					LatLng[] latLngs2 = pl.getLatLngs();
					Window.alert("Created polyline: " + pl.getRawLatLngs());
					break;
				case rectangle:
					Rectangle r = (Rectangle) event.getLayer();
					LatLng[] latLngs3 = r.getLatLngs();
					LatLngBounds bounds = r.getBounds();
					Window.alert("Created rectangle: " + r.getRawLatLngs());
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
