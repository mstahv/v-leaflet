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
package org.vaadin.addon.leaflet.client;

import java.util.ArrayList;
import java.util.List;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.Event;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.LatLngBounds;
import org.peimari.gleaflet.client.Map;
import org.peimari.gleaflet.client.MapOptions;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.MoveEndListener;
import org.peimari.gleaflet.client.control.Layers;
import org.peimari.gleaflet.client.resources.LeafletResourceInjector;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.shared.Bounds;
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
import org.peimari.gleaflet.client.Crs;

/**
 * 
 */
@Connect(LMap.class)
public class LeafletMapConnector extends AbstractHasComponentsConnector
		implements ElementResizeListener {

	static {
		LeafletResourceInjector.ensureInjected();
	}

	LeafletMapServerRpc rpc = RpcProxy.create(LeafletMapServerRpc.class, this);
	private Map map;
	private MapOptions options;
	private ArrayList<ServerConnector> updateChildren;
	private Layers layersControl;

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
				LatLngBounds bounds = toLeafletBounds(b);
				map.fitBounds(bounds);
			}

            @Override
            public void setMaxBounds(Bounds bounds) {
                map.setMaxBounds(toLeafletBounds(bounds));
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
            if(getState().maxBounds != null) {
                options.setMaxBounds(toLeafletBounds(getState().maxBounds));
            }
			if (getState().center != null) {
				options.setCenter(getCenterFromState());
			} else {
				options.setCenter(LatLng.create(0, 0));
			}
		
			/*
			 * See if CRS set. Maintain backwards compatability so that EPSG:3857
			 * used if nothing specified.
			 */
            if(getState().crsName != null) {
                options.setCrs(Crs.byName(getState().crsName));
            }

			if (getState().attributionPrefix == null) {
				options.setAttributionControl(false);
			}
			
			if(getState().maxZoom != null) {
				options.setMaxZoom(getState().maxZoom);
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

			if (getState().zoomToExtent != null) {
				Bounds b = getState().zoomToExtent;
				LatLng northEast = LatLng.create(b.getNorthEastLat(),
						b.getNorthEastLon());
				LatLng southWest = LatLng.create(b.getSouthWestLat(),
						b.getSouthWestLon());
				map.fitBounds(LatLngBounds.create(southWest, northEast));
			}

			map.addClickListener(new ClickListener() {
				public void onClick(MouseEvent event) {
					if (hasEventListener("click")) {
						rpc.onClick(new Point(event.getLatLng().getLatitude(),
								event.getLatLng().getLongitude()));
					}
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

	}

	private void updateChildrens() {
		if (map == null) {
			return;
		}
		if (updateChildren != null) {
			for (ServerConnector serverConnector : updateChildren) {
				if (serverConnector instanceof AbstractLeafletLayerConnector<?>) {
					AbstractLeafletLayerConnector<?> c = (AbstractLeafletLayerConnector<?>) serverConnector;
					c.updateIfDirty();
				}
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
			if (layersControl != null) {
				layersControl.removeLayer(layer);
			}
		}

		updateChildrens();
	}

	@Override
	public void onElementResize(ElementResizeEvent e) {
		// Without this it appears component is invalidly sized sometimes
		map.invalidateSize();
	}

	public void setLayersControl(Layers l) {
		this.layersControl = l;
	}

    public static LatLngBounds toLeafletBounds(Bounds b) {
        LatLng northEast = LatLng.create(b.getNorthEastLat(),
                b.getNorthEastLon());
        LatLng southWest = LatLng.create(b.getSouthWestLat(),
                b.getSouthWestLon());
        LatLngBounds bounds = LatLngBounds.create(southWest, northEast);
        return bounds;
    }

}
