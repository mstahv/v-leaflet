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
import java.util.Date;
import java.util.List;

import org.peimari.gleaflet.client.BaseLayerChangeListener;
import org.peimari.gleaflet.client.Circle;
import org.peimari.gleaflet.client.CircleMarker;
import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.ContextMenuListener;
import org.peimari.gleaflet.client.Crs;
import org.peimari.gleaflet.client.ErrorEvent;
import org.peimari.gleaflet.client.Event;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.LatLngBounds;
import org.peimari.gleaflet.client.Layer;
import org.peimari.gleaflet.client.LayersControlEvent;
import org.peimari.gleaflet.client.LocateOptions;
import org.peimari.gleaflet.client.LocationErrorListener;
import org.peimari.gleaflet.client.LocationEvent;
import org.peimari.gleaflet.client.LocationFoundListener;
import org.peimari.gleaflet.client.Map;
import org.peimari.gleaflet.client.MapOptions;
import org.peimari.gleaflet.client.Marker;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.MoveEndListener;
import org.peimari.gleaflet.client.OverlayAddListener;
import org.peimari.gleaflet.client.OverlayRemoveListener;
import org.peimari.gleaflet.client.Polyline;
import org.peimari.gleaflet.client.control.Layers;
import org.peimari.gleaflet.client.resources.LeafletResourceInjector;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.LeafletMapClientRpc;
import org.vaadin.addon.leaflet.shared.LeafletMapServerRpc;
import org.vaadin.addon.leaflet.shared.LeafletMapState;
import org.vaadin.addon.leaflet.shared.Point;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractHasComponentsConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.Connector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;

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

    public Point getSize()
    {
       org.peimari.gleaflet.client.Point clientPoint = map.getSize();
       return new Point(clientPoint.getY(), clientPoint.getX());
    }

    @Override
    protected Widget createWidget() {
        MapWidget mapWidget = new MapWidget();
        return mapWidget;
    }

    LocationFoundListener locationFoundListener;
    LocationErrorListener locationErrorListener;

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
            public void setCenter(Double lat, Double lon, Double zoom) {
                VConsole.log("To be center : " + lat + " " + lon + " ");
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

            @Override
            public void flyTo(Double lat, Double lon, Double zoom) {
                VConsole.log("To be flight to : " + lat + " " + lon + " ");
                if (zoom == null) {
                    zoom = map.getZoom();
                }
                LatLng center;
                if (lon == null) {
                    center = map.getBounds().getCenter();
                } else {
                    center = LatLng.create(lat, lon);
                }
                map.flyTo(center, zoom);
            }

            @Override
            public void setDragging(boolean dragging) {
                map.setDragging(dragging);
            }

            @Override
            public void setBoxZoom(boolean boxZoom) {
                map.setBoxZoom(boxZoom);
            }

            @Override
            public void setDoubleClickZoom(boolean doubleClickZoom) {
                map.setDoubleClickZoom(doubleClickZoom);
            }

            @Override
            public void setKeyboard(boolean keyboard) {
                map.setKeyboard(keyboard);
            }

            @Override
            public void locate(boolean watch, boolean highaccuracy, boolean updateView) {
                LocateOptions o = LocateOptions.create();
                o.setWatch(watch);
                o.setEnableHighAccuracy(highaccuracy);
                o.setView(updateView);
                if (locationFoundListener == null) {
                    locationFoundListener = new LocationFoundListener() {
                        private Date locationLastReportedTime;
                        private Timer lazyTimer;

                        @Override
                        public void onFound(LocationEvent event) {
                            if (hasEventListener("locate")) {
                                rpc.onLocate(U.toPoint(event.getLatLng()), event.getAccuracy(), event.getAltitude(), event.getSpeed());
                                if(lazyTimer == null) {
                                    getConnection().getServerRpcQueue().flush();
                                    lazyTimer = new Timer(){
                                        @Override
                                        public void run() {
                                            lazyTimer = null;
                                        }
                                    };
                                    lazyTimer.schedule(getState().minLocateInterval);
                                }
                            }

                            if (getState().updateLayersOnLocate != null) {
                                for (Connector c : getState().updateLayersOnLocate) {
                                    tryUpdateConnector(c, event);
                                }
                            }
                        }
                    };
                    locationErrorListener = new LocationErrorListener() {
                        @Override
                        public void onError(ErrorEvent event) {
                            rpc.onLocateError(event.geMessage(), event.getCode());
                        }
                    };
                    getMap().addLocationFoundListener(locationFoundListener);
                    getMap().addLocationErrorListener(locationErrorListener);
                }
                getMap().locate(o);
            }

            @Override
            public void stopLocate() {
                getMap().stopLocate();
            }

            @Override
            public void setScrollWheelZoom(boolean scrollWheelZoom) {
                map.setScrollWheelZoom(scrollWheelZoom);
            }

            @Override
            public void setTouchZoom(boolean touchZoom) {
                map.setTouchZoom(touchZoom);
            }

            @Override
            public void translate(int x, int y) {
                LatLng latLng = map.containerPointToLatLng(org.peimari.gleaflet.client.Point.create(x, y));
                rpc.onTranslate(new Point(latLng.getLatitude(), latLng.getLongitude()));
            }

           @Override
           public void getSize()
           {
              org.peimari.gleaflet.client.Point size = map.getSize();
              rpc.onSize(size.getX(), size.getY());
           }

        });

        getLayoutManager().addElementResizeListener(getWidget().getElement(),
                this);

    }

    private void tryUpdateConnector(Connector c, LocationEvent event) {
        // TODO refactor g-leaflet to contain PoinLayer or similar
        if (c instanceof LeafletMarkerConnector) {
            LeafletMarkerConnector markerConnector = (LeafletMarkerConnector) c;
            Marker lm = (Marker) markerConnector.getLayer();
            lm.setLatLng(event.getLatLng());
        } else if (c instanceof LeafletCircleConnector) {
            LeafletCircleConnector circleConnector = (LeafletCircleConnector) c;
            Circle circle = (Circle) circleConnector.getLayer();
            circle.setLatLng(event.getLatLng());
            // Circle is assumed to reprecent the accuracy
            circle.setRadius(event.getAccuracy());
        } else if (c instanceof LeafletCircleMarkerConnector) {
            LeafletCircleMarkerConnector circleConnector = (LeafletCircleMarkerConnector) c;
            CircleMarker circle = (CircleMarker) circleConnector.getLayer();
            circle.setLatLng(event.getLatLng());
        } else if (c instanceof LeafletPolylineConnector) {
            LeafletPolylineConnector connector = (LeafletPolylineConnector) c;
            Polyline pl = (Polyline) connector.getLayer();
            pl.addLatLng(event.getLatLng());
            // TODO consider adding a feature to trim old points
        }

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
            if (getState().customMapOptionsJson != null) {
                options = JSONParser.parseStrict(getState().customMapOptionsJson).isObject().getJavaScriptObject().cast();
            } else {
                options = MapOptions.create();
            }
            if (getState().maxBounds != null) {
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
            if (getState().newCrsName != null) {
                if (getState().newCrsProjection != null) {
                options.setCrs(Crs.add(getState().newCrsName, getState().newCrsProjection,
                        getState().newCrsA, getState().newCrsB,
                        getState().newCrsC, getState().newCrsD));
                } else {
                    options.setCrs(Crs.add(getState().newCrsName,
                            getState().newCrsMinX, getState().newCrsMinY,
                            getState().newCrsMaxX, getState().newCrsMaxY,
                            getState().newCrsA, getState().newCrsB,
                            getState().newCrsC, getState().newCrsD));
                }
            } else if (getState().crsName != null) {
                options.setCrs(Crs.byName(getState().crsName));
            }

            if (getState().attributionPrefix == null) {
                options.setAttributionControl(false);
            }

            if (getState().dragging != null) {
                options.setDragging(getState().dragging);
            }

            if (getState().touchZoom != null) {
                options.setTouchZoom(getState().touchZoom);
            }

            if (getState().doubleClickZoom != null) {
                options.setDoubleClickZoom(getState().doubleClickZoom);
            }

            if (getState().boxZoom != null) {
                options.setBoxZoom(getState().boxZoom);
            }

            if (getState().scrollWheelZoom != null) {
                options.setScrollWheelZoom(getState().scrollWheelZoom);
            }

            if (getState().zoomSnap != null) {
                options.setZoomSnap(getState().zoomSnap);
            }

            if (getState().zoomDelta != null) {
                options.setZoomDelta(getState().zoomDelta);
            }

            if (getState().keyboard != null) {
                options.setKeyboard(getState().keyboard);
            }

            if (getState().minZoom != null) {
                options.setMinZoom(getState().minZoom);
            }
            if (getState().maxZoom != null) {
                options.setMaxZoom(getState().maxZoom);
            }

            double zoom = 15;
            if (getState().zoomLevel != null) {
                zoom = getState().zoomLevel;
            }
            options.setZoom(zoom);
            final Element mapElement = getWidget().getElement().getFirstChildElement();
            map = createMap(mapElement, options);
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
                // With certain Vaadin layouts, like SplitPanel, the size is not
                // fixed properly yet, so defer the fitBounds call
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
                    public void execute() {
                        map.fitBounds(LatLngBounds.create(southWest, northEast));
                    }
                });
            }

            map.addClickListener(new ClickListener() {
                @Override
               public void onClick(MouseEvent event) {
                    if (hasEventListener("click")) {

                        // Add mouse details
                        MouseEventDetails details = MouseEventDetailsBuilder
                                .buildMouseEventDetails(event.getNativeEvent(), getWidget().getElement());

                        rpc.onClick(new Point(event.getLatLng().getLatitude(), event.getLatLng().getLongitude()),
                                details
                        );
                    }
                }
            });

            map.addContextMenuListener(new ContextMenuListener() {
                @Override
                public void onContextMenu(MouseEvent event) {
                    if (hasEventListener("contextmenu")) {
                        // Add mouse details
                        MouseEventDetails details = MouseEventDetailsBuilder
                                .buildMouseEventDetails(event.getNativeEvent(), getWidget().getElement());

                        rpc.onContextMenu(new Point(event.getLatLng().getLatitude(),
                                event.getLatLng().getLongitude()), details
                        );
                    }
                }
            });

            map.addBaseLayerChangeListener(new BaseLayerChangeListener() {
                @Override
                public void onBaseLayerChange(LayersControlEvent event) {
                    if (hasEventListener("baselayerchange")) {
                        rpc.onBaseLayerChange(event.getName());
                    }
                }
            });

            map.addOverlayAddListener(new OverlayAddListener() {
                @Override
                public void onOverlayAdd(LayersControlEvent event) {
                    if (hasEventListener("overlayadd")) {
                        rpc.onOverlayAdd(event.getName());
                    }
                }
            });

            map.addOverlayRemoveListener(new OverlayRemoveListener() {
                @Override
                public void onOverlayRemove(LayersControlEvent event) {
                    if (hasEventListener("overlayremove")) {
                        rpc.onOverlayRemove(event.getName());
                    }
                }
            });

            MoveEndListener moveEndListener = new MoveEndListener() {

                @Override
                public void onMoveEnd(Event event) {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                        @Override
                        public void execute() {
                            reportViewPortToServer();
                        }
                    });
                }
            };

            map.addMoveEndListener(moveEndListener);

            if (getState().width != null && !getState().width.contains("%")) {
                // fixed size for the leaflet map, report size manually to the
                // server
                reportViewPortToServer();
            }

            // Force one size invalidation on initial render. Needed e.g. for
            // MapInGridDetailsRow, no idea why
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            map.invalidateSize();
                        }
                    });
                }
            });

        } else {
            // extent, zoom etc, must not be updated here, see client rpc...
        }

        updateChildrens();

    }

    protected Map createMap(final Element mapElement, MapOptions options) {
        return Map.create(mapElement, options);
    }

    protected void reportViewPortToServer() {
        rpc.onMoveEnd(new Bounds(map.getBounds().toBBoxString()),
                new Point(map.getCenter().getLatitude(),
                        map.getCenter().getLongitude()), map.getZoom());
        if (getState().registeredEventListeners != null
                && getState().registeredEventListeners
                        .contains("moveend")) {
            getConnection().sendPendingVariableChanges();
        }
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
            // Instance of check for Popup's that don't extend AbstractLeafletLayerConnector
            if (componentConnector instanceof AbstractLeafletLayerConnector) {
                AbstractLeafletLayerConnector<?> c = (AbstractLeafletLayerConnector<?>) componentConnector;
                Layer layer = c.getLayer();
                try {
                    map.removeLayer(layer);
                    if (layersControl != null) {
                        layersControl.removeLayer(layer);
                    }
                } catch (Exception e) {
                    VConsole.log("Removing failed, possibly due to timing issue...");
                }
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

    public Point getMapPixelPosition() {
        return new Point(getWidget().getAbsoluteTop(), getWidget().getAbsoluteLeft());
    }

}
