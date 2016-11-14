package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Crs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.leaflet.shared.LeafletMapClientRpc;
import org.vaadin.addon.leaflet.shared.LeafletMapServerRpc;
import org.vaadin.addon.leaflet.shared.LeafletMapState;
import org.vaadin.addon.leaflet.control.AbstractControl;
import org.vaadin.addon.leaflet.control.LLayers;
import org.vaadin.addon.leaflet.control.LScale;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.server.Extension;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.vaadin.addon.leaflet.jsonmodels.BasicMap;

/**
 *
 */
public class LMap extends AbstractComponentContainer {

    class State {

        boolean dragging;
        public boolean touchZoom;
        public boolean doubleClickZoom;
        public boolean boxZoom;
        public boolean scrollWheelZoom;
        public boolean keyboard;
        public LLayers lLayers;
        public boolean lZoom;
    }

    private State readWriteState = new State();

    private boolean rendered = false;

    private List<Component> components = new ArrayList<Component>();

    private Bounds bounds;

    private Crs lCrs;

    public LMap() {
        setSizeFull();
        registerRpc(new LeafletMapServerRpc() {
            @Override
            public void onClick(Point p, MouseEventDetails d) {
                fireEvent(new LeafletClickEvent(LMap.this, p, d));
            }

            @Override
            public void onMoveEnd(Bounds bounds, Point center, double zoomlevel) {
                getState(false).zoomLevel = zoomlevel;
                getState(false).center = center;
                LMap.this.bounds = bounds;
                fireEvent(new LeafletMoveEndEvent(LMap.this, bounds, center, zoomlevel));
            }

            @Override
            public void onContextMenu(Point p, MouseEventDetails d) {
                fireEvent(new LeafletContextMenuEvent(LMap.this, p, d));
            }

            @Override
            public void onBaseLayerChange(String name) {
                fireEvent(new LeafletBaseLayerChangeEvent(LMap.this, name));
            }

            @Override
            public void onLocate(Point location, Double accuracy, Double altitude) {
                fireEvent(new LeafletLocateEvent(LMap.this, location, accuracy, altitude));
            }

            @Override
            public void onLocateError(String error, int code) {
                // TODO
                error.length();
            }

        });

    }

    @Override
    public void beforeClientResponse(boolean initial) {
        rendered = true;
        super.beforeClientResponse(initial);
    }

    @Override
    public void detach() {
        super.detach();
        rendered = false;
    }

    @Override
    protected LeafletMapState getState(boolean markAsDirty) {
        return (LeafletMapState) super.getState(markAsDirty);
    }

    @Override
    protected LeafletMapState getState() {
        return (LeafletMapState) super.getState();
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException();
    }

    /**
     * Add a base layer with given name.
     *
     * @param baseLayer
     * @param name to be used in LayerControl, null if layer should not be
     * displayed in the layer control
     */
    public void addBaseLayer(LeafletLayer baseLayer, String name) {
        addLayer(baseLayer);
        LLayers control = getLayersControl();
        if (control != null) {
            control.addBaseLayer(baseLayer, name);
        }
    }

    public LZoom getZoomControl() {
        for (Extension e : getExtensions()) {
            if (e instanceof LZoom) {
                return (LZoom) e;
            }
        }
        LZoom lZoom = new LZoom();
        addExtension(lZoom);
        return lZoom;
    }

    public LLayers getLayersControl() {
        for (Extension e : getExtensions()) {
            if (e instanceof LLayers) {
                return (LLayers) e;
            }
        }
        LLayers lLayers = new LLayers();
        addExtension(lLayers);
        return lLayers;
    }

    public void addControl(AbstractControl control) {
        addExtension(control);
    }

    public void removeControl(AbstractControl control) {
        control.remove();
    }

    public void addOverlay(LeafletLayer overlay, String name) {
        addLayer(overlay);
        LLayers control = getLayersControl();
        if (control != null) {
            control.addOverlay(overlay, name);
        }
    }

    public void addLayer(LeafletLayer layer) {
        addComponent(layer);
    }

    public void removeLayer(LeafletLayer layer) {
        removeComponent(layer);
    }

    @Override
    public void addComponent(Component c) {
        if (!(c instanceof LeafletLayer)) {
            throw new IllegalArgumentException(
                    "only instances of LeafletLayer (AbstractLeafletLayer or LLayerGroup) allowed");
        }
        super.addComponent(c);
        components.add(c);
        markAsDirty(); // ?? is this really needed
    }

    @Override
    public void removeComponent(Component c) {
        super.removeComponent(c);
        components.remove(c);
        if (hasControl(LLayers.class)) {
            LLayers layersControl = getLayersControl();
            if (layersControl != null) {
                layersControl.removeLayer((LeafletLayer) c);
            }
        }
        markAsDirty(); // ?? is this really needed
    }

    @Override
    public int getComponentCount() {
        return components.size();
    }

    public boolean hasControl(Class<? extends AbstractControl> leafletControl) {
        for (Extension e : getExtensions()) {
            if (leafletControl.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasComponent(Component component) {
        return components.contains(component);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    public void setView(Double lat, Double lon, Double zoom) {
        getState(!rendered).center = new Point(lat, lon);
        if (zoom != null) {
            getState(!rendered).zoomLevel = zoom;
        }
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setCenter(lat, lon, zoom);
        }
    }

    public void setCenter(double lat, double lon) {
        setView(lat, lon, null);
    }

    public void setCenter(Point center, Double zoom) {
        setView(center.getLat(), center.getLon(), zoom);
    }

    public void flyTo(Point center, Double zoom) {
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).flyTo(center.getLat(), center.getLon(), zoom);
        } else {
            setCenter(center, zoom);
        }
    }

    public void setCenter(Point center) {
        setCenter(center, null);
    }

    public void setCenter(com.vividsolutions.jts.geom.Point jtsPoint) {
        Point point = new Point();
        point.setLat(jtsPoint.getY());
        point.setLon(jtsPoint.getX());
        setCenter(point);
    }

    public void setZoomLevel(double zoomLevel) {
        getState(!rendered).zoomLevel = zoomLevel;
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setCenter(null, null,
                    zoomLevel);
        }
    }

    public void addLocateListener(LeafletLocateListener listener) {
        addListener("locate", LeafletLocateEvent.class, listener,
                LeafletLocateListener.METHOD);
    }

    public void removeLocateListener(LeafletLocateListener listener) {
        removeListener("locate", LeafletLocateEvent.class, listener);
    }

    public void addClickListener(LeafletClickListener listener) {
        addListener("click", LeafletClickEvent.class, listener,
                LeafletClickListener.METHOD);
    }

    public void removeClickListener(LeafletClickListener listener) {
        removeListener("click", LeafletClickEvent.class, listener);
    }

    public void addMoveEndListener(LeafletMoveEndListener moveEndListener) {
        addListener("moveend", LeafletMoveEndEvent.class, moveEndListener,
                LeafletMoveEndListener.METHOD);
    }

    public void removeMoveEndListener(LeafletMoveEndListener moveEndListener) {
        removeListener("moveend", LeafletMoveEndEvent.class, moveEndListener);
    }

    public void addContextMenuListener(LeafletContextMenuListener listener) {
        addListener("contextmenu", LeafletContextMenuEvent.class, listener,
                LeafletContextMenuListener.METHOD);
    }

    public void removeContextMenuListener(LeafletContextMenuListener listener) {
        removeListener("contextmenu", LeafletContextMenuEvent.class, listener);
    }

    public void addBaseLayerChangeListener(LeafletBaseLayerChangeListener listener) {
        addListener("baselayerchange", LeafletBaseLayerChangeEvent.class, listener,
                LeafletBaseLayerChangeListener.METHOD);
    }

    public void removeBaseLayerChangeListener(LeafletBaseLayerChangeListener listener) {
        removeListener("baselayerchange", LeafletBaseLayerChangeEvent.class, listener);
    }

    public void setCenter(Bounds bounds) {
        setCenter(bounds.getCenter());
    }

    public Double getZoomLevel() {
        return getState(false).zoomLevel;
    }

    public void zoomToExtent(Bounds bounds) {
        this.bounds = bounds;

        if (bounds.getNorthEastLat() == bounds.getSouthWestLat() && bounds.getNorthEastLon() == bounds.getSouthWestLon()) {
            // if only single point, don't touch zoom level
            setCenter(bounds.getNorthEastLat(), bounds.getNorthEastLon());
        } else {
            getState(!rendered).center = bounds.getCenter();
            getState(!rendered).zoomToExtent = bounds;
            if (rendered) {
                // If already on the screen, use RPC instead to avoid "full paint"
                getRpcProxy(LeafletMapClientRpc.class).zoomToExtent(bounds);
            }
        }

    }

    public void zoomToExtent(Geometry geometry) {
        Bounds bounds = JTSUtil.getBounds(geometry);
        zoomToExtent(bounds);
    }

    /**
     * Gets the bounds of the maps "viewport". The bounds are not known during
     * initialization as we cannot be sure about the size of the map. Thus, if
     * you for example want to initialize the map content based on the currently
     * visible viewport, add a MoveEndListener to your map, which is notified
     * after the initial render and each time the user moves or zooms the
     * viewport.
     *
     * @return the last know bounds (reported by client or set by zoomToExtennt)
     * or null if not known.
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * Calculates extent of all contained components that are not "baselayers".
     */
    public void zoomToContent() {
        Geometry contentBounds = getContentBounds();
        if (contentBounds != null) {
            zoomToExtent(contentBounds);
        }
    }

    public Geometry getContentBounds() {
        Collection<Geometry> gc = new ArrayList<Geometry>();
        for (Component c : this) {
            LeafletLayer l = (LeafletLayer) c;
            Geometry geometry = l.getGeometry();
            if (geometry != null) {
                gc.add(geometry);
            }
        }
        if (!gc.isEmpty()) {
            return new GeometryFactory().buildGeometry(gc);
        }
        return null;
    }

    /**
     * @param values
     * @deprecated, use addControl() instead
     */
    @Deprecated
    public void setControls(List<Control> values) {
        if (values != null) {
            for (Control control : values) {
                switch (control) {
                    case zoom:
                        addControl(new LZoom());
                        break;
                    case scale:
                        addControl(new LScale());
                    default:
                        break;
                }
            }
        }
    }

    public Point getCenter() {
        return getState(false).center;
    }

    public void setAttributionPrefix(String prefix) {
        getState().attributionPrefix = prefix;
    }

    public String getAttributionPrefix() {
        return getState(false).attributionPrefix;
    }

    public void zoomToContent(LeafletLayer l) {
        Geometry geometry = l.getGeometry();
        if (geometry != null) {
            zoomToExtent(geometry);
        }
    }

    /**
     * Sets the area into where the viewport is restricted.
     *
     * @param bounds
     */
    public void setMaxBounds(Bounds bounds) {
        getState(!rendered).maxBounds = bounds;
        if (rendered) {
            // If already on the screen, use RPC instead to avoid "full paint"
            getRpcProxy(LeafletMapClientRpc.class).setMaxBounds(bounds);
        }
    }

    /**
     * Sets the area into where the viewport is restricted.
     *
     * @param bounds
     */
    public void setMaxBounds(Geometry bounds) {
        setMaxBounds(JTSUtil.getBounds(bounds));
    }

    public void setMinZoom(int minZoom) {
        getState().minZoom = minZoom;
    }

    public void setMaxZoom(int maxZoom) {
        getState().maxZoom = maxZoom;
    }

    public Crs getCrs() {
        return lCrs;
    }

    /**
     * Specify the CRS to use for the whole Map.
     *
     * @param lc The CRS to use (one from a limited range)
     */
    public void setCrs(Crs lc) {
        this.lCrs = lc;
        getState().crsName = lc.getName();
    }

    /**
     * Adds and uses a new Crs definition and makes it immediately available for
     * use inside a Map. The new Crs extends Crs.Simple, uses the Projection as
     * specified in the parameters and an affine transform as specified by the
     * a, b, c, d parameters). For the meaning of the affine transform
     * parameters, see: http://leafletjs.com/reference.html#transformation.
     *
     * @param name Name for the new Crs.
     * @param projection Name of the projection for this new Crs. It needs to be
     * the name of a valid projection defined in L.Projection (LonLat,
     * SphericalMercator, Mercator).
     * @param a transformation parameter a
     * @param b transformation parameter b
     * @param c transformation parameter c
     * @param d transformation parameter d
     */
    public void setCustomCrs(String name, String projection, double a, double b, double c, double d) {
        getState().newCrsName = name;
        getState().newCrsProjection = projection;
        getState().newCrsA = a;
        getState().newCrsB = b;
        getState().newCrsC = c;
        getState().newCrsD = d;
    }

    private BasicMap customMapOptions;

    public void setCustomInitOption(String key, boolean b) {
        if (customMapOptions == null) {
            customMapOptions = new BasicMap();
        }
        customMapOptions.put(key, b);
        getState().customMapOptionsJson = customMapOptions.asJson();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        boolean switchToReadOnly = readOnly && !isReadOnly();
        boolean switchToReadWrite = !readOnly && isReadOnly();
        if (switchToReadOnly) {
            readWriteState.dragging = isDraggingEnabled();
            readWriteState.touchZoom = isTouchZoomEnabled();
            readWriteState.doubleClickZoom = isDoubleZoomEnabled();
            readWriteState.boxZoom = isBoxZoomEnabled();
            readWriteState.scrollWheelZoom = isScrollWheelZoomEnabled();
            readWriteState.keyboard = isKeyboardZoomEnabled();
            readWriteState.lLayers = getLayersControl();
            readWriteState.lZoom = getZoomControl().isEnabled();

            setDraggingEnabled(!readOnly);
            setTouchZoomEnabled(!readOnly);
            setDoubleClickZoomEnabled(!readOnly);
            setBoxZoomEnabled(!readOnly);
            setScrollWheelZoomEnabled(!readOnly);
            setKeyboardEnabled(!readOnly);

            getLayersControl().remove();
            getZoomControl().setEnabled(!readOnly);
        }

        if (switchToReadWrite) {
            setDraggingEnabled(readWriteState.dragging);
            setTouchZoomEnabled(readWriteState.touchZoom);
            setDoubleClickZoomEnabled(readWriteState.doubleClickZoom);
            setBoxZoomEnabled(readWriteState.boxZoom);
            setScrollWheelZoomEnabled(readWriteState.scrollWheelZoom);
            setKeyboardEnabled(readWriteState.keyboard);
            addControl(new LLayers(readWriteState.lLayers));
            getZoomControl().setEnabled(readWriteState.lZoom);
        }

        super.setReadOnly(readOnly);
    }

    /**
     * @see
     * <a href="http://leafletjs.com/reference-1.0.0.html#map-dragging">Leaflet.js
     * doc</a>
     */
    public void setDraggingEnabled(boolean dragging) {
        getState(!rendered).dragging = dragging;
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setDragging(dragging);
        }
    }

    ;
	
	/**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-dragging">Leaflet.js doc</a>
     */
	public boolean isDraggingEnabled() {
        Boolean dragging = getState(false).dragging;
        return dragging != null ? dragging : true;
    }

    /**
     * @see
     * <a href="http://leafletjs.com/reference-1.0.0.html#map-touchzoom">Leaflet.js
     * doc</a>
     */
    public void setTouchZoomEnabled(boolean touchZoom) {
        getState(!rendered).touchZoom = touchZoom;
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setTouchZoom(touchZoom);
        }
    }

    ;
	
	/**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-touchzoom">Leaflet.js doc</a>
     */
	public boolean isTouchZoomEnabled() {
        Boolean touchZoom = getState(false).touchZoom;
        return touchZoom != null ? touchZoom : true;
    }

    /**
     * @see
     * <a href="http://leafletjs.com/reference-1.0.0.html#map-doubleclickzoom">Leaflet.js
     * doc</a>
     */
    public void setDoubleClickZoomEnabled(boolean doubleClickZoom) {
        getState(!rendered).doubleClickZoom = doubleClickZoom;
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setDoubleClickZoom(doubleClickZoom);
        }
    }

    ;
	
	/**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-doubleclickzoom">Leaflet.js doc</a>
     */
	public boolean isDoubleZoomEnabled() {
        Boolean doubleClickZoom = getState(false).doubleClickZoom;
        return doubleClickZoom != null ? doubleClickZoom : true;
    }

    /**
     * @see
     * <a href="http://leafletjs.com/reference-1.0.0.html#map-boxzoom">Leaflet.js
     * doc</a>
     */
    public void setBoxZoomEnabled(boolean boxZoom) {
        getState(!rendered).boxZoom = boxZoom;
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setBoxZoom(boxZoom);
        }
    }

    ;
	
	/**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-boxzoom">Leaflet.js doc</a>
     */
	public boolean isBoxZoomEnabled() {
        Boolean boxZoom = getState(false).boxZoom;
        return boxZoom != null ? boxZoom : true;
    }

    /**
     * @see
     * <a href="http://leafletjs.com/reference-1.0.0.html#map-scrollwheelzoom">Leaflet.js
     * doc</a>
     */
    public void setScrollWheelZoomEnabled(boolean scrollWheelZoom) {
        getState(!rendered).scrollWheelZoom = scrollWheelZoom;
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setScrollWheelZoom(scrollWheelZoom);
        }
    }

    ;
	
	/**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-scrollwheelzoom">Leaflet.js doc</a>
     */
	public boolean isScrollWheelZoomEnabled() {
        Boolean scrollWheelZoom = getState(false).scrollWheelZoom;
        return scrollWheelZoom != null ? scrollWheelZoom : true;
    }

    /**
     * @see
     * <a href="http://leafletjs.com/reference-1.0.0.html#map-keyboard">Leaflet.js
     * doc</a>
     */
    public void setKeyboardEnabled(boolean keyboard) {
        getState(!rendered).keyboard = keyboard;
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setKeyboard(keyboard);
        }
    }

    ;
	
	/**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-keyboard">Leaflet.js doc</a>
     */
	public boolean isKeyboardZoomEnabled() {
        Boolean keyboard = getState(false).keyboard;
        return keyboard != null ? keyboard : true;
    }

    /**
     * Tries to detect the users geolocation.
     */
    public void locate() {
        locate(false, false, false);
    }

    /**
     * Tries to detect the users geolocation.
     *
     * @param watch true if location should be updated continuously
     * @param highaccuracy true if high accuracy should be requested
     * @param updateView true if the viewport should be centered to the latest
     * position, with appropriate zoom level
     */
    public void locate(boolean watch, boolean highaccuracy, boolean updateView) {
        getRpcProxy(LeafletMapClientRpc.class).locate(watch, highaccuracy, updateView);
    }

    /**
     * Stops detecting users geolocation.
     */
    public void stopLocate() {
        getRpcProxy(LeafletMapClientRpc.class).stopLocate();
    }

    /**
     * @param millis the minimum interval in milliseconds that server side gets
     * notified about the new location (via LeafletLocateListener), if watch
     * option is used. Can be handy if you want to limit the amount of client
     * server traffic. The default is 5000ms.
     */
    public void setMinLocatationUpdateInterval(int millis) {
        getState().minLocateInterval = millis;
    }

    /**
     * Updates given layers directly on the client side when position is
     * located. Typically used to show the location of the user without any
     * latency and extra client-server communication.
     *
     * @param layers that should be updated, only LMarker, LCircle and
     * LCircleMarker are currently supported. If the layer is of type LCircle,
     * the radius is set to accuracy of the location event
     */
    public void setLayersToUpdateOnLocate(AbstractLeafletLayer... layers) {
        getState().updateLayersOnLocate = layers;
    }

}
