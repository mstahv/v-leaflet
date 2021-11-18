package org.vaadin.addon.leaflet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.vaadin.addon.leaflet.control.AbstractControl;
import org.vaadin.addon.leaflet.control.LLayers;
import org.vaadin.addon.leaflet.control.LScale;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addon.leaflet.jsonmodels.BasicMap;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Crs;
import org.vaadin.addon.leaflet.shared.LeafletMapClientRpc;
import org.vaadin.addon.leaflet.shared.LeafletMapServerRpc;
import org.vaadin.addon.leaflet.shared.LeafletMapState;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.server.Extension;
import com.vaadin.server.SerializableConsumer;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

/**
 *
 */
public class LMap extends AbstractComponentContainer {

    private SerializableConsumer<Point> translationCallback;
    private SerializableConsumer<Point> sizeCallback;
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

    private List<Component> components = new ArrayList<>();

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
            public void onOverlayAdd(String name) {
                fireEvent(new LeafletOverlayAddEvent(LMap.this, name));
            }

            @Override
            public void onOverlayRemove(String name) {
                fireEvent(new LeafletOverlayRemoveEvent(LMap.this, name));
            }

            @Override
            public void onLocate(Point location, Double accuracy, Double altitude, Double speed) {
                fireEvent(new LeafletLocateEvent(LMap.this, location, accuracy, altitude, speed));
            }

            @Override
            public void onLocateError(String error, int code) {
                // TODO
                error.length();
            }

            @Override
            public void onTranslate(Point p) {
                if(translationCallback != null) {
                    translationCallback.accept(p);
                    translationCallback = null;
                }
            }

           @Override
           public void onSize(double x, double y)
           {
              if (sizeCallback != null)
              {
                 sizeCallback.accept(new Point(x, y));
                 sizeCallback = null;
              }
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
     * @param baseLayer the layer to be added
     * @param name      to be used in LayerControl, null if layer should not be
     *                  displayed in the layer control
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

    public void setCenter(org.locationtech.jts.geom.Point jtsPoint) {
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

    public Registration addLocateListener(LeafletLocateListener listener) {
        return addListener("locate", LeafletLocateEvent.class, listener,
                LeafletLocateListener.METHOD);
    }

    public Registration addClickListener(LeafletClickListener listener) {
        return addListener("click", LeafletClickEvent.class, listener,
                LeafletClickListener.METHOD);
    }

    public Registration addMoveEndListener(LeafletMoveEndListener moveEndListener) {
        return addListener("moveend", LeafletMoveEndEvent.class, moveEndListener,
                LeafletMoveEndListener.METHOD);
    }

    public Registration addContextMenuListener(LeafletContextMenuListener listener) {
        return addListener("contextmenu", LeafletContextMenuEvent.class, listener,
                LeafletContextMenuListener.METHOD);
    }

    @SuppressWarnings("unused")
    public Registration addBaseLayerChangeListener(LeafletBaseLayerChangeListener listener) {
        return addListener("baselayerchange", LeafletBaseLayerChangeEvent.class, listener,
                LeafletBaseLayerChangeListener.METHOD);
    }

    public Registration addOverlayAddListener(LeafletOverlayAddListener listener) {
        return addListener("overlayadd", LeafletOverlayAddEvent.class, listener,
                LeafletOverlayAddListener.METHOD);
    }

    public Registration addOverlayRemoveListener(LeafletOverlayRemoveListener listener) {
        return addListener("overlayremove", LeafletOverlayRemoveEvent.class, listener,
                LeafletOverlayRemoveListener.METHOD);
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
        Collection<Geometry> gc = new ArrayList<>();
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
     * @param controls the list of controls to be shown
     * @deprecated use addControl() instead
     */
    @Deprecated
    public void setControls(List<Control> controls) {
        if (controls != null) {
            for (Control control : controls) {
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
     * @param bounds the bounds of where the viewport of the map should be limited to
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
     * @param bounds the bounds of where the viewport of the map should be limited to
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
     * @param name       Name for the new Crs.
     * @param projection Name of the projection for this new Crs. It needs to be
     *                   the name of a valid projection defined in L.Projection (LonLat,
     *                   SphericalMercator, Mercator).
     * @param a          transformation parameter a
     * @param b          transformation parameter b
     * @param c          transformation parameter c
     * @param d          transformation parameter d
     */
    public void setCustomCrs(String name, String projection, double a, double b, double c, double d) {
        getState().newCrsName = name;
        getState().newCrsProjection = projection;
        getState().newCrsA = a;
        getState().newCrsB = b;
        getState().newCrsC = c;
        getState().newCrsD = d;
    }


    /**
     * Adds and uses a new Crs definition and makes it immediately available for
     * use inside a Map. The new Crs extends Crs.Simple, uses a plat-carré projection with the
     * extents given by the min_* and max_* parameters. For the meaning of the affine transform
     * parameters, see: http://leafletjs.com/reference.html#transformation.
     *
     * @param name Name for the new Crs.
     * @param min_x the minimum x value
     * @param min_y the minimum y value
     * @param max_x the maximum x value
     * @param max_y the maximum y value
     * @param a transformation parameter a
     * @param b transformation parameter b
     * @param c transformation parameter c
     * @param d transformation parameter d
     */
    public void setCustomCrs(String name, double min_x, double min_y, double max_x, double max_y,
            double a, double b, double c, double d) {
        getState().newCrsName = name;
        getState().newCrsMinX = min_x;
        getState().newCrsMinY = min_y;
        getState().newCrsMaxX = max_x;
        getState().newCrsMaxY = max_y;
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

        if (readOnly != isReadOnly()) {
            getState().readOnly = readOnly;
        }
    }

    /**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-dragging">Leaflet.js
     * doc</a>
     *
     * @param dragging true if dragging map should be possible
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
     *
     * @return true if dragging is enabled
     */
    public boolean isDraggingEnabled() {
        Boolean dragging = getState(false).dragging;
        return dragging != null ? dragging : true;
    }

    /**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-touchzoom">Leaflet.js
     * doc</a>
     *
     * @param touchZoom true if touch zoom should be enabled
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
     *
     * @return true if touch zoom should be enabled
     */
    public boolean isTouchZoomEnabled() {
        Boolean touchZoom = getState(false).touchZoom;
        return touchZoom != null ? touchZoom : true;
    }

    /**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-doubleclickzoom">Leaflet.js
     * doc</a>
     *
     * @param doubleClickZoom true if double click zooming should be enabled
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
     *
     * @return true if double click zooming is enabled
     */
    public boolean isDoubleZoomEnabled() {
        Boolean doubleClickZoom = getState(false).doubleClickZoom;
        return doubleClickZoom != null ? doubleClickZoom : true;
    }

    /**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-boxzoom">Leaflet.js
     * doc</a>
     *
     * @param boxZoom true if box zoom mode should be enabled
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
     *
     * @return true if box zoom mode is enabled
     */
    public boolean isBoxZoomEnabled() {
        Boolean boxZoom = getState(false).boxZoom;
        return boxZoom != null ? boxZoom : true;
    }

    /**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-scrollwheelzoom">Leaflet.js
     * doc</a>
     *
     * @param scrollWheelZoom true if zooming with scroll wheel should be enabled
     */
    public void setScrollWheelZoomEnabled(boolean scrollWheelZoom) {
        getState(!rendered).scrollWheelZoom = scrollWheelZoom;
        if (rendered) {
            getRpcProxy(LeafletMapClientRpc.class).setScrollWheelZoom(scrollWheelZoom);
        }
    }

    /**
     * @see <a href="https://leafletjs.com/reference-1.3.2.html#map-zoomsnap">Leaflet.js doc</a>
     *
     * @param zoomSnap
     */
    public void setZoomSnap(Double zoomSnap) {
        getState(!rendered).zoomSnap = zoomSnap;
    }

    /**
     * @see <a href="https://leafletjs.com/reference-1.3.2.html#map-zoomdelta">Leaflet.js doc</a>
     *
     * @param zoomDelta
     */
    public void setZoomDelta(Double zoomDelta) {
        getState(!rendered).zoomDelta = zoomDelta;
    }

    /**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-scrollwheelzoom">Leaflet.js doc</a>
     *
     * @return true if scroll wheel zoom is enabled
     */
    public boolean isScrollWheelZoomEnabled() {
        Boolean scrollWheelZoom = getState(false).scrollWheelZoom;
        return scrollWheelZoom != null ? scrollWheelZoom : true;
    }

    /**
     * @see <a href="http://leafletjs.com/reference-1.0.0.html#map-keyboard">Leaflet.js
     * doc</a>
     *
     * @param keyboard true if keyboard usage should be enabled
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
     *
     * @return true if keyboard zoom is enabled
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
     * @param watch        true if location should be updated continuously
     * @param highAccuracy true if high accuracy should be requested
     * @param updateView   true if the viewport should be centered to the latest
     *                     position, with appropriate zoom level
     */
    public void locate(boolean watch, boolean highAccuracy, boolean updateView) {
        getRpcProxy(LeafletMapClientRpc.class).locate(watch, highAccuracy, updateView);
    }

    /**
     * Stops detecting users geolocation.
     */
    public void stopLocate() {
        getRpcProxy(LeafletMapClientRpc.class).stopLocate();
    }

    /**
     * @param millis the minimum interval in milliseconds that server side gets
     *               notified about the new location (via LeafletLocateListener), if watch
     *               option is used. Can be handy if you want to limit the amount of client
     *               server traffic. The default is 5000ms.
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
     *               LCircleMarker are currently supported. If the layer is of type LCircle,
     *               the radius is set to accuracy of the location event
     */
    public void setLayersToUpdateOnLocate(AbstractLeafletLayer... layers) {
        getState().updateLayersOnLocate = layers;
    }

    @Override
    public boolean isReadOnly() {
        Boolean readOnly = getState(false).readOnly;
        return readOnly != null && readOnly;
    }

    /**
     * Translates given parameters (x/y pixel coordinates relative to the map)
     * to actual coordinates on the client side and passes the values to the callback.
     * <p>
     * Note, only one callback is allowed at once.
     *
     * @param x the x pixel coordinate
     * @param y the y pixel coordinate
     * @param callback the callback to notify when translation is done
     */
    public void translatePixelCoordinates(int x, int y, SerializableConsumer<Point> callback) {
        if(translationCallback == null) {
            getRpcProxy(LeafletMapClientRpc.class).translate(x, y);
            translationCallback = callback;
        } else {
            throw new IllegalStateException("Only one active call to translatePixelCoordinates is allowed");
        }
    }

    public void getSize(SerializableConsumer<Point> callback)
    {
       if (sizeCallback == null)
       {
          getRpcProxy(LeafletMapClientRpc.class).getSize();
          sizeCallback = callback;
       } else
       {
          throw new IllegalStateException(
                "Only one active call to getSize is allowed");
       }
    }

}
