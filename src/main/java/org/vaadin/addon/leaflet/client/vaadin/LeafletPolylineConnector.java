package org.vaadin.addon.leaflet.client.vaadin;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.Polyline;
import org.peimari.gleaflet.client.PolylineOptions;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.core.client.JsArray;
import com.vaadin.client.JsArrayObject;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LPolyline.class)
public class LeafletPolylineConnector extends
		AbstractLeafletVectorConnector<LeafletPolylineState, PolylineOptions> {

	private Polyline marker;

	@Override
	protected void update() {
		if (marker != null) {
			removeLayerFromParent();
			marker.removeClickListener();
		}
		if (getState().points == null) {
			return;
		}

		PolylineOptions options = createOptions();
		marker = Polyline.create(getLatLngsArray(), options);

		marker.addClickListener(new ClickListener() {

			@Override
			public void onClick(MouseEvent event) {
				rpc.onClick(new Point(event.getLatLng().getLatitude(), event
						.getLatLng().getLongitude()));
			}
		});

		addToParent(marker);
	}

	@Override
	public ILayer getLayer() {
		return marker;
	}

	protected JsArray<LatLng> getLatLngsArray() {
		JsArray<LatLng> latlngs = JsArrayObject.createArray().cast();
		for (Point p : getState().points) {
			latlngs.push(LatLng.create(p.getLat(), p.getLon()));
		}
		return latlngs;
	}

}
