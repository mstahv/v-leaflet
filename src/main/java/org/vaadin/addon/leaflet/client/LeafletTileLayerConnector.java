package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.LeafletTileLayerState;
import org.vaadin.addon.leaflet.shared.LeafletTileLayerServerRpc;
import org.peimari.gleaflet.client.*;
import org.vaadin.addon.leaflet.shared.EventId;

import com.vaadin.client.communication.*;
import com.vaadin.shared.ui.*;

@Connect(org.vaadin.addon.leaflet.LTileLayer.class)
public class LeafletTileLayerConnector extends
        AbstractLeafletLayerConnector<TileLayerOptions> {

    protected Layer layer;
    protected LeafletTileLayerServerRpc tileLayerServerRpc = RpcProxy.create(LeafletTileLayerServerRpc.class, this);

    @Override
    public LeafletTileLayerState getState() {
        return (LeafletTileLayerState) super.getState();
    }

    @Override
    protected TileLayerOptions createOptions() {
        TileLayerOptions o = TileLayerOptions.create();
        LeafletTileLayerState s = getState();
        o.setAttribution(s.attributionString);
        if (s.detectRetina != null && s.detectRetina) {
            o.setDetectRetina(true);
        }
        if (s.subDomains != null) {
            o.setSubDomains(s.subDomains);
        }
        if (s.minZoom != null) {
            o.setMinZoom(s.minZoom);
        }
        if (s.maxZoom != null) {
            o.setMaxZoom(s.maxZoom);
        }
        if (s.maxNativeZoom != null) {
            o.setMaxNativeZoom(s.maxNativeZoom);
        }
        if (s.tms != null && s.tms) {
            o.setTms(true);
        }
        if (s.opacity != null) {
            o.setOpacity(s.opacity);
        }
        if (s.zIndex != null) {
            o.setZindex(s.zIndex);
        }
        if (s.noWrap != null) {
            o.setNoWrap(s.noWrap);
        }
        if (s.bounds != null) {
            o.setBounds(LatLngBounds.create(LatLng.create(s.bounds.getSouthWestLat(), s.bounds.getSouthWestLon()),
                    LatLng.create(s.bounds.getNorthEastLat(), s.bounds.getNorthEastLon())));
        }
        if (s.customOptions != null) {
            for (String keyName : s.customOptions.keySet()) {
                o.setCustomOption(keyName, s.customOptions.get(keyName));
            }
        }
        return o;
    }

    @Override
    protected void update() {
        if (layer != null) {
            removeLayerFromParent();
        }
        TileLayerOptions o = createOptions();
        layer = createTileLayer(o);
        TileLayer tileLayer = (TileLayer) layer;
        if (hasEventListener(EventId.LOAD)) {
            tileLayer.addLoadListener(new LoadListener() {
                @Override
                public void onLoad(Event event) {
                    tileLayerServerRpc.onLoad();
                }
            });
        }
        if (hasEventListener(EventId.LOADING)) {
            tileLayer.addLoadingListener(new LoadingListener() {
                @Override
                public void onLoading(Event event) {
                    tileLayerServerRpc.onLoading();
                }
            });
        }
        addToParent(layer);
    }

    protected TileLayer createTileLayer(TileLayerOptions o) {
        return TileLayer.create(getState().url, o);
    }

    @Override
    public Layer getLayer() {
        return layer;
    }
}
