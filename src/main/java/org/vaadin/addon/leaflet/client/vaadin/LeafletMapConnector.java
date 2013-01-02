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

import java.util.HashSet;
import java.util.Set;

import org.discotools.gwt.leaflet.client.LeafletResourceInjector;
import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.controls.scale.Scale;
import org.discotools.gwt.leaflet.client.controls.scale.ScaleOptions;
import org.discotools.gwt.leaflet.client.crs.epsg.EPSG3857;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.raster.TileLayer;
import org.discotools.gwt.leaflet.client.map.Map;
import org.discotools.gwt.leaflet.client.map.MapOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.discotools.gwt.leaflet.client.widget.MapWidget;
import org.vaadin.addon.leaflet.LeafletMap;
import org.vaadin.addon.leaflet.shared.BaseLayer;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractHasComponentsConnector;
import com.vaadin.shared.ui.Connect;

/**
 * 
 * @author mattitahvonenitmill
 */
@Connect(LeafletMap.class)
public class LeafletMapConnector extends AbstractHasComponentsConnector {

	static {
		LeafletResourceInjector.ensureInjected();
	}

	LeafletMapServerRpc rpc = RpcProxy.create(LeafletMapServerRpc.class, this);
	private Map map;
	private EPSG3857 vCRS_EPSG3857 = new EPSG3857();
	private String id = "leafletmap" + count++;
	private MapOptions options;
	private Set<ILayer> layers = new HashSet<ILayer>();
	static private int count = 0;

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
		MapWidget mapWidget = new MapWidget(id);
		mapWidget.setHeight("100%");
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
			map = new Map(id, options);

			setBaseLayers();

			// Add Scale Control
			ScaleOptions scaleOptions = new ScaleOptions();
			Scale scale = new Scale(scaleOptions);
			scale.addTo(map);

			map.invalidateSize(false);

		} else {
			if (stateChangeEvent.getChangedProperties().contains("baseLayers")) {
				setBaseLayers();
			}
			if (stateChangeEvent.getChangedProperties().contains("center")
					&& getState().center != null) {
				LatLng center = getCenterFromState();
				map.setView(center, map.getZoom(), false);
			}
			if (stateChangeEvent.getChangedProperties().contains("zoomLevel")
					&& getState().zoomLevel != null) {
				// TODO
			}
		}

		// TODO check if this is really needed
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				map.invalidateSize(false);
			}
		});
	}

	private void setBaseLayers() {
		// clear old layers
		for (ILayer l : layers) {
			map.removeLayer(l);
		}
		layers.clear();
		// add layers from state
		BaseLayer[] baseLayers = getState().getBaseLayers();
		if (baseLayers != null) {
			for (BaseLayer baseLayer : baseLayers) {
				Options tileOptions = new Options();
				tileOptions.setProperty("attribution",
						baseLayer.getAttributionString());
				TileLayer layer = new TileLayer(baseLayer.getUrl(), tileOptions);
				map.addLayer(layer);
				layers.add(layer);
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
		// TODO figure out a sane way to ensure the correct order of children

	}

}
