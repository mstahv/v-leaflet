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
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.vaadin.addon.leaflet.jsonmodels.BasicMap;

/**
 *
 */
public class LMap extends AbstractComponentContainer {

    private boolean rendered = false;

    private List<Component> components = new ArrayList<Component>();

    private Bounds bounds;

    private Crs lCrs;

    public LMap() {
        setSizeFull();
        registerRpc(new LeafletMapServerRpc() {
            @Override
            public void onClick(Point p) {
                fireEvent(new LeafletClickEvent(LMap.this, p));
            }

            @Override
            public void onMoveEnd(Bounds bounds, Point center, double zoomlevel) {
                getState(false).zoomLevel = zoomlevel;
                getState(false).center = center;
                LMap.this.bounds = bounds;
                fireEvent(new LeafletMoveEndEvent(LMap.this, bounds, center, zoomlevel));
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

    public void setCenter(Bounds bounds) {
        setCenter(bounds.getCenter());
    }

    public Double getZoomLevel() {
        return getState(false).zoomLevel;
    }

    public void zoomToExtent(Bounds bounds) {
        this.bounds = bounds;

        if (bounds.getNorthEastLat() == bounds.getSouthWestLat() && bounds.getNorthEastLon() == bounds.getNorthEastLon()) {
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

    private BasicMap customMapOptions;

    public void setCustomInitOption(String key, boolean b) {
        if (customMapOptions == null) {
            customMapOptions = new BasicMap();
        }
        customMapOptions.put(key, b);
        getState().customMapOptionsJson = customMapOptions.asJson();
    }
}
