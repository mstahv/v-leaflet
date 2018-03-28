package org.vaadin.addon.leaflet.client;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.*;
import org.vaadin.addon.leaflet.shared.EventId;
import org.vaadin.addon.leaflet.shared.LeafletGridLayerServerRpc;
import org.vaadin.addon.leaflet.shared.LeafletGridLayerState;

@Connect(org.vaadin.addon.leaflet.LGridLayer.class)
public class LeafletGridLayerConnector extends AbstractLeafletLayerConnector<GridLayerOptions> {

    protected Layer layer;
    protected LeafletGridLayerServerRpc gridLayerServerRpc = RpcProxy.create(LeafletGridLayerServerRpc.class, this);

    public LeafletGridLayerState getState() {
        return (LeafletGridLayerState) super.getState();
    }

    @Override
    protected GridLayerOptions createOptions() {
        GridLayerOptions o = GridLayerOptions.create();
        LeafletGridLayerState s = getState();

        if (s.attributionString != null) {
            o.setAttribution(s.attributionString);
        }
        if (s.minNativeZoom != null) {
            o.setMinNativeZoom(s.minNativeZoom);
        }
        if (s.maxNativeZoom != null) {
            o.setMaxNativeZoom(s.maxNativeZoom);
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
        return o;
    }

    @Override
    protected void update() {
        if (layer != null) {
            removeLayerFromParent();
        }
        GridLayerOptions o = createOptions();
        layer = createGridLayer(o);
        GridLayer gridLayer = (GridLayer) layer;
        if (hasEventListener(EventId.LOAD)) {
            gridLayer.addLoadListener(new LoadListener() {
                @Override
                public void onLoad(Event event) {
                    gridLayerServerRpc.onLoad();
                }
            });
        }
        if (hasEventListener(EventId.LOADING)) {
            gridLayer.addLoadingListener(new LoadingListener() {
                @Override
                public void onLoading(Event event) {
                    gridLayerServerRpc.onLoading();
                }
            });
        }
        addToParent(layer);
    }

    protected GridLayer createGridLayer(GridLayerOptions o) {
        return GridLayer.create(o);
    }

    @Override
    public Layer getLayer() {
        return layer;
    }
}
