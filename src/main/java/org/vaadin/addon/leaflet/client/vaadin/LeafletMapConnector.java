/*
 * Copyright 2012 Vaadin Community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.addon.leaflet.client.vaadin;

import java.util.ArrayList;
import java.util.List;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.Crs;
import org.peimari.gleaflet.client.Event;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.LatLngBounds;
import org.peimari.gleaflet.client.Map;
import org.peimari.gleaflet.client.MapOptions;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.MoveEndListener;
import org.peimari.gleaflet.client.control.Layers;
import org.peimari.gleaflet.client.control.Scale;
import org.peimari.gleaflet.client.control.ScaleOptions;
import org.peimari.gleaflet.client.resources.LeafletResourceInjector;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.LayerControlInfo;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractHasComponentsConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;

/**
 * 
 * @author mattitahvonenitmill
 * @param <V>
 */
@Connect(LMap.class)
public class LeafletMapConnector<V> extends AbstractHasComponentsConnector
		implements ElementResizeListener {

	static {
		LeafletResourceInjector.ensureInjected();
	}

	LeafletMapServerRpc rpc = RpcProxy.create(LeafletMapServerRpc.class, this);
	private Map map;
	private MapOptions options;
	private ArrayList<ServerConnector> updateChildren;

	// Must have this one here, because of removal (and we want to preserve the
	// states)
	private Layers layerControl;

	@Override
	public MapWidget getWidget() {
		return (MapWidget) super.getWidget();
	}

	@Override
	public LeafletMapState getState() {
		return (LeafletMapState) super.getState();
	}

	@Override
	protected Widget createWidget() {
		MapWidget mapWidget = new MapWidget();
		return mapWidget;
	}

	@Override
	protected void init() {
		super.init();
		/*
		 * Working with state can be really painfull in some cases. E.g. if we
		 * keep extent, center and stuff in state, we'll get false "changes" if
		 * children are updated at the same time. In maps case we might get some
		 * nasty rounding erros -> weird "effects". Thus we have to do all live
		 * updates via RPC and store same stuff in state for initial renderings
		 * and reattaches. It is sooo easy...
		 */
		registerRpc(LeafletMapClientRpc.class, new LeafletMapClientRpc() {

			@Override
			public void setCenter(Double lat, Double lon, Integer zoom) {
				if (zoom == null) {
					zoom = map.getZoom();
				}
				LatLng center;
				if (lon == null) {
					center = map.getBounds().getCenter();
				} else {
					center = LatLng.create(lat, lon);
				}
				map.setView(center, zoom);
			}

			@Override
			public void zoomToExtent(Bounds b) {
				LatLng northEast = LatLng.create(b.getNorthEastLat(),
						b.getNorthEastLon());
				LatLng southWest = LatLng.create(b.getSouthWestLat(),
						b.getSouthWestLon());
				map.fitBounds(LatLngBounds.create(southWest, northEast));
			}
		});

		getLayoutManager().addElementResizeListener(getWidget().getElement(),
				this);

	}

	@Override
	public void onUnregister() {
		super.onUnregister();
		getLayoutManager().removeElementResizeListener(
				getWidget().getElement(), this);
	}

	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		if (map == null) {
			updateChildren = new ArrayList<ServerConnector>(getChildren());
			options = MapOptions.create();
			if (getState().center != null) {
				options.setCenter(getCenterFromState());
			} else {
				options.setCenter(LatLng.create(0, 0));
			}
			options.setCrs(Crs.EPSG3857());

			if (getState().attributionPrefix == null) {
				options.setAttributionControl(false);
			}
			int zoom = 15;
			if (getState().zoomLevel != null) {
				zoom = getState().zoomLevel;
			}
			options.setZoom(zoom);
			map = Map.create(getWidget().getElement().getFirstChildElement(),
					options);
			if (getState().attributionPrefix != null) {
				map.getAttributionControl().setPrefix(
						getState().attributionPrefix);
			}

			for (Control c : getState().controls) {
				switch (c) {
				case attribution:
					// NOP
					break;
				case baselayers:
					if (layerControl == null) {
						layerControl = Layers.create();
						map.addControl(layerControl);
					}
					break;
				case scale:
					// Add Scale Control
					ScaleOptions scaleOptions = ScaleOptions.create();
					Scale scale = Scale.create(scaleOptions);
					map.addControl(scale);
					break;
				case zoom:
					// TODO how to remove this? Default.
					break;
				case overlays:
					// Lets not do anything here

				default:
					break;
				}
			}

			map.addClickListener(new ClickListener() {
				public void onClick(MouseEvent event) {
					rpc.onClick(new Point(event.getLatLng().getLatitude(),
							event.getLatLng().getLongitude()));
				}
			});

			MoveEndListener moveEndListener = new MoveEndListener() {

				@Override
				public void onMoveEnd(Event event) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {

						@Override
						public void execute() {
							rpc.onMoveEnd(new Bounds(map.getBounds()
									.toBBoxString()), map.getZoom());
							if (getState().registeredEventListeners != null
									&& getState().registeredEventListeners
											.contains("moveend")) {
								getConnection().sendPendingVariableChanges();
							}
						}
					});
				}
			};

			map.addMoveEndListener(moveEndListener);

		} else {
			// extent, zoom etc, must not be updated here, see client rpc...
		}

		updateChildrens();

		if (getState().controls.contains(Control.overlays)) {
			if (layerControl == null) {
				layerControl = Layers.create();
				map.addControl(layerControl);
			}
			if (stateChangeEvent.hasPropertyChanged("layerContolInfo")) {

				for (ServerConnector connector : getChildren()) {
					if (!(connector instanceof AbstractLeafletLayerConnector<?>)) {
						continue;
					}
					AbstractLeafletLayerConnector<?> layerConnector = (AbstractLeafletLayerConnector<?>) connector;
					LayerControlInfo layerControlInfo = getState().layerContolInfo
							.get(layerConnector);

					if (layerControlInfo != null && layerControlInfo.name != null) {
						if (layerControlInfo.baseLayer) {
							layerControl.addBaseLayer(
									layerConnector.getLayer(), layerControlInfo.name);
						} else {
							layerControl.addOverlay(layerConnector.getLayer(),
									layerControlInfo.name);
						}
					}
				}
			}

		}

	}

	private void updateChildrens() {
		if (map == null) {
			return;
		}
		if (updateChildren != null) {
			for (ServerConnector serverConnector : updateChildren) {

				AbstractLeafletLayerConnector<?> c = (AbstractLeafletLayerConnector<?>) serverConnector;
				c.update();
				c.markUpdated();

			}
			updateChildren = null;
		}
	}

	private LatLng getCenterFromState() {
		LatLng center = LatLng.create(getState().center.getLat(),
				getState().center.getLon());
		return center;
	}

	public Map getMap() {
		return map;
	}

	@Override
	public void updateCaption(ComponentConnector connector) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectorHierarchyChange(
			ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
		List<ComponentConnector> oldChildren = connectorHierarchyChangeEvent
				.getOldChildren();
		updateChildren = new ArrayList<ServerConnector>();
		for (ServerConnector componentConnector : getChildren()) {
			if (!oldChildren.contains(componentConnector)) {
				updateChildren.add(componentConnector);
			}
			oldChildren.remove(componentConnector);
		}
		for (ComponentConnector componentConnector : oldChildren) {
			AbstractLeafletLayerConnector<?> c = (AbstractLeafletLayerConnector<?>) componentConnector;
			ILayer layer = c.getLayer();
			map.removeLayer(layer);
			if (layerControl != null) {
				layerControl.removeLayer(c.getLayer());
			}
		}

		updateChildrens();
	}

	@Override
	public void onElementResize(ElementResizeEvent e) {
		// Without this it appears component is invalidly sized sometimes
		map.invalidateSize();
	}

}
