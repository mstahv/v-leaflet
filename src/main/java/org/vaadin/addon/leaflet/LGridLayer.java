package org.vaadin.addon.leaflet;

import org.locationtech.jts.geom.Geometry;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.EventId;
import org.vaadin.addon.leaflet.shared.LeafletGridLayerServerRpc;
import org.vaadin.addon.leaflet.shared.LeafletGridLayerState;

public class LGridLayer extends AbstractLeafletLayer {

    public LGridLayer() {
        super();
        registerRpc(new LeafletGridLayerServerRpc() {
            @Override
            public void onLoad() {
                fireEvent(new LeafletLoadingEvent(LGridLayer.this));
            }

            @Override
            public void onLoading() {
                fireEvent(new LeafletLoadEvent(LGridLayer.this));
            }
        });
    }

    @Override
   protected LeafletGridLayerState getState() {
        return (LeafletGridLayerState) super.getState();
    }

    public String getAttributionString() {
        return getState().attributionString;
    }

    public void setAttributionString(String attributionString) {
        getState().attributionString = attributionString;
    }

    public Integer getMinNativeZoom() {
        return getState().minNativeZoom;
    }

    public void setMinNativeZoom(Integer minNativeZoom) {
        getState().minNativeZoom = minNativeZoom;
    }

    public Integer getMaxNativeZoom() {
        return getState().maxNativeZoom;
    }

    public void setMaxNativeZoom(Integer maxNativeZoom) {
        getState().maxNativeZoom = maxNativeZoom;
    }

    public Double getOpacity() {
        return getState().opacity;
    }

    public void setOpacity(Double opacity) {
        getState().opacity = opacity;
    }

    public Integer getZindex() {
        return getState().zIndex;
    }

    public void setZindex(int zIndex) {
        getState().zIndex = zIndex;
    }

    public Boolean getNoWrap() {
        return getState().noWrap;
    }

    public void setNoWrap(Boolean noWrap) {
        getState().noWrap = noWrap;
    }

    public void setBounds(Bounds bounds) {
        getState().bounds = bounds;
    }

    public Bounds getBounds() {
        return getState().bounds;
    }

    public void setTileSize(int tileSize)
    {
       getState().tileSize = tileSize;
    }

    public int getTileSize()
    {
       return getState().tileSize;
    }

    @Override
    public Geometry getGeometry() {
        return null;
    }

    public void addLoadListener(LeafletLoadListener listener) {
        addListener(EventId.LOAD, LeafletLoadEvent.class, listener,
                LeafletLoadListener.METHOD);
    }

    public void addLoadingListener(LeafletLoadingListener listener) {
        addListener(EventId.LOADING, LeafletLoadingEvent.class, listener,
                LeafletLoadingListener.METHOD);
    }
}
