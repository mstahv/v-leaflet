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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.discotools.gwt.leaflet.client.LeafletResourceInjector;
import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.controls.attribution.AttributionImpl;
import org.discotools.gwt.leaflet.client.controls.layers.Layers;
import org.discotools.gwt.leaflet.client.controls.scale.Scale;
import org.discotools.gwt.leaflet.client.controls.scale.ScaleOptions;
import org.discotools.gwt.leaflet.client.crs.epsg.EPSG3857;
import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.raster.TileLayer;
import org.discotools.gwt.leaflet.client.layers.raster.WmsLayer;
import org.discotools.gwt.leaflet.client.map.Map;
import org.discotools.gwt.leaflet.client.map.MapOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.discotools.gwt.leaflet.client.types.LatLngBounds;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.core.client.JsArrayString;
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
 */
@Connect(LMap.class)
public class LeafletMapConnector extends AbstractHasComponentsConnector implements ElementResizeListener {

	static {
		LeafletResourceInjector.ensureInjected();
	}

	LeafletMapServerRpc rpc = RpcProxy.create(LeafletMapServerRpc.class, this);
	private Map map;
	private EPSG3857 vCRS_EPSG3857 = new EPSG3857();
	private MapOptions options;
	private java.util.Map<BaseLayer, ILayer> layers = new LinkedHashMap<BaseLayer, ILayer>();
	private ArrayList<ServerConnector> updateChildren;
	private HashMap<String, String> connectorIdToNameMap = new HashMap<String, String>();

	// Must have this one here, because of removal (and we want to preserve the
	// states)
	private Layers lControl;

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
					center = new LatLng(lat, lon);
				}
				map.setView(center, zoom, false);
			}

			@Override
			public void zoomToExtent(Bounds b) {
				LatLng northEast = new LatLng(b.getNorthEastLat(), b
						.getNorthEastLon());
				LatLng southWest = new LatLng(b.getSouthWestLat(), b
						.getSouthWestLon());
				map.fitBounds(new LatLngBounds(southWest, northEast));
			}
		});
		
		getLayoutManager().addElementResizeListener(getWidget().getElement(), this);

	}
	
	@Override
	public void onUnregister() {
		super.onUnregister();
		getLayoutManager().removeElementResizeListener(getWidget().getElement(), this);
	}

	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		if (map == null) {
			updateChildren = new ArrayList<ServerConnector>(getChildren());
			options = new MapOptions();
			if (getState().center != null) {
				options.setCenter(getCenterFromState());
			} else {
				options.setCenter(new LatLng(0, 0));
			}
			options.setProperty("crs", vCRS_EPSG3857);

			if (getState().attributionPrefix == null) {
				options.setProperty("attributionControl", false);
			}
			int zoom = 15;
			if (getState().zoomLevel != null) {
				zoom = getState().zoomLevel;
			}
			options.setZoom(zoom);
			map = new Map(getWidget().getElement().getFirstChildElement(),
					options);
			if (getState().attributionPrefix != null) {
				AttributionImpl.setPrefix(
						map.getJSObject().getProperty("attributionControl"),
						getState().attributionPrefix);
			}
			setBaseLayers();

			for (Control c : getState().controls) {
				switch (c) {
				case attribution:
					// NOP
					break;
				case baselayers:
					Options opts = new Options();
					for (BaseLayer l : layers.keySet()) {
						opts.setProperty(l.getName(), layers.get(l));
					}
					if (lControl == null) {
						lControl = new Layers(opts, new Options(),
								new Options());
					}
					map.addControl(lControl);
					break;
				case scale:
					// Add Scale Control
					ScaleOptions scaleOptions = new ScaleOptions();
					Scale scale = new Scale(scaleOptions);
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

			EventHandler<?> handler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					rpc.onClick(new Point(event.getLatLng().lat(), event
							.getLatLng().lng()));
				}
			};

			EventHandlerManager.addEventHandler(map, Events.click, handler);

			handler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					rpc.onMoveEnd(new Bounds(map.getBounds().toBBoxString()),
							map.getZoom());
					if (getState().registeredEventListeners != null
							&& getState().registeredEventListeners
									.contains("moveend")) {
						getConnection().sendPendingVariableChanges();
					}
				}
			};
			EventHandlerManager.addEventHandler(map, Events.moveend, handler);

		} else {
			if (stateChangeEvent.hasPropertyChanged("baseLayers")) {
				setBaseLayers();
			}
			// extent, zoom etc, must not be updated here, see client rpc...
		}

		updateChildrens();

		if (getState().controls.contains(Control.overlays)) {
			if (lControl == null) {
				lControl = new Layers(new Options(), new Options(),
						new Options());
			}
			for (ServerConnector connector : getChildren()) {
				if (!(connector instanceof AbstractLeafletLayerConnector<?>)) {
					continue;
				}
				AbstractLeafletLayerConnector<?> layerGroupConnector = (AbstractLeafletLayerConnector<?>) connector;
				String name = layerGroupConnector.getState().name;
				if (name == null) {
					continue;
				}
				connectorIdToNameMap.put(layerGroupConnector.getConnectorId(),
						name);
				lControl.addOverlay(layerGroupConnector.getLayer(), name);
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

	private void setBaseLayers() {
		// clear old layers
		for (ILayer l : layers.values()) {
			map.removeLayer(l);
		}
		layers.clear();
		// add layers from state
		BaseLayer[] baseLayers = getState().getBaseLayers();
		if (baseLayers != null) {
			for (BaseLayer baseLayer : baseLayers) {
				// suck big time in V7, can't access the raw json, should use
				// e.g. gson and serialize as string in state
				Options tileOptions = new Options();
				tileOptions.setProperty("attribution",
						baseLayer.getAttributionString());
				if (baseLayer.getDetectRetina() != null
						&& baseLayer.getDetectRetina()) {
					tileOptions.setProperty("detectRetina", true);
				}
				if (baseLayer.getSubDomains() != null) {
					JsArrayString domain = JsArrayString.createArray().cast();
					for (String a : baseLayer.getSubDomains()) {
						domain.push(a);
					}
					tileOptions.setProperty("subdomains",
							(JSObject) domain.cast());
				}
				if (baseLayer.getMaxZoom() != null) {
					tileOptions.setProperty("maxZoom", baseLayer.getMaxZoom());
				}
				if (baseLayer.getTms() != null && baseLayer.getTms()) {
					tileOptions.setProperty("tms", true);
				}
				if (baseLayer.getOpacity() != null) {
					tileOptions.setProperty("opacity", baseLayer.getOpacity());
				}

				if (baseLayer.getWms() != null && baseLayer.getWms() == true) {
					if (baseLayer.getLayers() != null) {
						tileOptions
								.setProperty("layers", baseLayer.getLayers());
					}
					if (baseLayer.getStyles() != null) {
						tileOptions
								.setProperty("styles", baseLayer.getStyles());
					}
					if (baseLayer.getFormat() != null) {
						tileOptions
								.setProperty("format", baseLayer.getFormat());
					}
					if (baseLayer.getTransparent() != null
							&& baseLayer.getTransparent()) {
						tileOptions.setProperty("transparent", true);
					}
					if (baseLayer.getVersion() != null) {
						tileOptions.setProperty("version",
								baseLayer.getVersion());
					}
					WmsLayer layer = new WmsLayer(baseLayer.getUrl(),
							tileOptions);
					map.addLayer(layer);
					layers.put(baseLayer, layer);
				} else {
					TileLayer layer = new TileLayer(baseLayer.getUrl(),
							tileOptions);
					map.addLayer(layer);
					layers.put(baseLayer, layer);
				}
			}
		}
	}

	private LatLng getCenterFromState() {
		LatLng center = new LatLng(getState().center.getLat(),
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
			map.removeLayer(c.getLayer());
			// TODO: This does not work atm.
			if (c instanceof LeafletLayerGroupConnector) {
				String name = connectorIdToNameMap.remove(c.getConnectorId());
				if (name == null) {
					continue;
				}
				Layers removed = lControl.removeLayer(name);
				map.removeControl(lControl);
				map.addControl(removed);
				lControl = removed;
			}
		}

		updateChildrens();
	}

	@Override
	public void onElementResize(ElementResizeEvent e) {
		// Without this it appears component is invalidly sized sometimes
		map.invalidateSize(false);
	}

}
