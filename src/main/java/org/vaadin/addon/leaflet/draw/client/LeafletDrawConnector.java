package org.vaadin.addon.leaflet.draw.client;

import org.peimari.gleaflet.client.Circle;
import org.peimari.gleaflet.client.EditableMap;
import org.peimari.gleaflet.client.FeatureGroup;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.Marker;
import org.peimari.gleaflet.client.Polygon;
import org.peimari.gleaflet.client.Polyline;
import org.peimari.gleaflet.client.draw.Draw;
import org.peimari.gleaflet.client.draw.DrawControlOptions;
import org.peimari.gleaflet.client.draw.LayerCreatedEvent;
import org.peimari.gleaflet.client.draw.LayerCreatedListener;
import org.peimari.gleaflet.client.draw.LayerType;
import org.peimari.gleaflet.client.draw.LayersDeletedEvent;
import org.peimari.gleaflet.client.draw.LayersDeletedListener;
import org.peimari.gleaflet.client.draw.LayersEditedEvent;
import org.peimari.gleaflet.client.draw.LayersEditedListener;
import org.peimari.gleaflet.client.resources.LeafletDrawResourceInjector;
import org.vaadin.addon.leaflet.client.AbstractControlConnector;
import org.vaadin.addon.leaflet.client.AbstractLeafletLayerConnector;
import org.vaadin.addon.leaflet.client.LeafletCircleConnector;
import org.vaadin.addon.leaflet.client.LeafletFeatureGroupConnector;
import org.vaadin.addon.leaflet.client.LeafletMarkerConnector;
import org.vaadin.addon.leaflet.client.LeafletPolylineConnector;
import org.vaadin.addon.leaflet.client.U;
import org.vaadin.addon.leaflet.draw.LDraw;

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
		final LeafletFeatureGroupConnector fgc = (LeafletFeatureGroupConnector) getState().featureGroup;
		FeatureGroup layerGroup = (FeatureGroup) fgc.getLayer();
		options.setEditableFeatureGroup(layerGroup);
		Draw l = Draw.create(options);

		getMap().addLayerCreatedListener(new LayerCreatedListener() {

			@Override
			public void onCreate(LayerCreatedEvent event) {
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
		
		getMap().addLayersEditedListener(new LayersEditedListener() {
			
			@Override
			public void onEdit(LayersEditedEvent event) {
				ILayer[] layers = event.getLayers().getLayers();
				for (ILayer iLayer : layers) {
					AbstractLeafletLayerConnector<?> c = fgc.getConnectorFor(iLayer);
					if(c != null) {
						if (c instanceof LeafletMarkerConnector) {
							LeafletMarkerConnector mc = (LeafletMarkerConnector) c;
							rpc.markerModified(mc, U.toPoint(((Marker) iLayer).getLatLng()));
						} else if (c instanceof LeafletCircleConnector) {
							LeafletCircleConnector cc = (LeafletCircleConnector) c;
							Circle circle = (Circle) cc.getLayer();
							rpc.circleModified(cc, U.toPoint(circle.getLatLng()), circle.getRadius());
						} else if (c instanceof LeafletPolylineConnector) {
							// polygon also gets here
							LeafletPolylineConnector plc = (LeafletPolylineConnector) c;
							Polyline polyline = (Polyline) plc.getLayer();
							rpc.polylineModified(plc, U.toPointArray(polyline.getLatLngs()));
						}
					}
				}
			}
		});
		
		getMap().addLayersDeletedListener(new LayersDeletedListener() {
			
			@Override
			public void onDelete(LayersDeletedEvent event) {
				ILayer[] layers = event.getLayers().getLayers();
				for (ILayer iLayer : layers) {
					AbstractLeafletLayerConnector<?> c = fgc.getConnectorFor(iLayer);
					rpc.layerDeleted(c);
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
