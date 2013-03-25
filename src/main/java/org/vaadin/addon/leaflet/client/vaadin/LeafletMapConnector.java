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
import java.util.List;

import org.discotools.gwt.leaflet.client.LeafletResourceInjector;
import org.discotools.gwt.leaflet.client.Options;
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
import com.vaadin.shared.ui.Connect;

/**
 * 
 * @author mattitahvonenitmill
 */
@Connect(LMap.class)
public class LeafletMapConnector extends AbstractHasComponentsConnector {

	static {
		LeafletResourceInjector.ensureInjected();
	}

	LeafletMapServerRpc rpc = RpcProxy.create(LeafletMapServerRpc.class, this);
	private Map map;
	private EPSG3857 vCRS_EPSG3857 = new EPSG3857();
	private MapOptions options;
	private java.util.Map<BaseLayer, ILayer> layers = new HashMap<BaseLayer, ILayer>();
	private ArrayList<ServerConnector> updateChildren;

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
		// getLayoutManager().addElementResizeListener(getWidget().getElement(),
		// new ElementResizeListener() {
		// @Override
		// public void onElementResize(ElementResizeEvent e) {
		// map.invalidateSize(false);
		// }
		// });

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
			int zoom = 15;
			if (getState().zoomLevel != null) {
				zoom = getState().zoomLevel;
			}
			options.setZoom(zoom);
			map = new Map(getWidget().getElement(), options);

			setBaseLayers();

			for (Control c : getState().controls) {
				switch (c) {
				case attribution:
					// TODO how to remove this? Default.
					break;
				case layers:
					Options opts = new Options();
					for (BaseLayer l : layers.keySet()) {
						opts.setProperty(l.getName(), layers.get(l));
					}
					Layers lControl = new Layers(opts, new Options(),
							new Options());
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
					rpc.onMoveEnd(new Bounds(map.getBounds().toBBoxString()));
				}
			};
			EventHandlerManager.addEventHandler(map, Events.moveend, handler);

		} else {
			if (stateChangeEvent.hasPropertyChanged("baseLayers")) {
				setBaseLayers();
			}
			if (stateChangeEvent.hasPropertyChanged("zoomToExtent")) {
				Bounds b = getState().zoomToExtent;
				LatLng northEast = new LatLng(b.getNorthEastLat(),
						b.getNorthEastLon());
				LatLng southWest = new LatLng(b.getSouthWestLat(),
						b.getSouthWestLon());
				map.fitBounds(new LatLngBounds(southWest, northEast));
			} else if (stateChangeEvent.hasPropertyChanged(
					"center")
					&& getState().center != null) {
				LatLng center = getCenterFromState();
				int zoom = map.getZoom();
				if (stateChangeEvent.hasPropertyChanged(
						"zoomLevel")
						&& getState().zoomLevel != null) {
					zoom = getState().zoomLevel;
				}
				map.setView(center, zoom, false);
			} else if (stateChangeEvent.hasPropertyChanged(
					"zoomLevel")
					&& getState().zoomLevel != null) {
				map.setZoom(getState().zoomLevel);
			}
		}

		updateChildrens();

		// Without this it appears component is invalidly sized sometimes
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				map.invalidateSize(false);
			}
		});
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
				TileLayer layer = new TileLayer(baseLayer.getUrl(), tileOptions);
				map.addLayer(layer);
				layers.put(baseLayer, layer);
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
		}

		updateChildrens();
	}

}
