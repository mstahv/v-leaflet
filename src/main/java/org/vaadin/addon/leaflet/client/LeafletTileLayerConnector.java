package org.vaadin.addon.leaflet.client;

import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.GridLayer;
import org.peimari.gleaflet.client.GridLayerOptions;
import org.peimari.gleaflet.client.TileLayer;
import org.peimari.gleaflet.client.TileLayerOptions;
import org.vaadin.addon.leaflet.shared.LeafletTileLayerState;

@Connect(org.vaadin.addon.leaflet.LTileLayer.class)
public class LeafletTileLayerConnector extends LeafletGridLayerConnector {

    @Override
    public LeafletTileLayerState getState() {
        return (LeafletTileLayerState) super.getState();
    }

    @Override
    protected TileLayerOptions createOptions() {
        TileLayerOptions o = super.createOptions().cast();
        LeafletTileLayerState s = getState();

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
        if (s.tms != null && s.tms) {
            o.setTms(true);
        }
        if (s.customOptions != null) {
            for (String keyName : s.customOptions.keySet()) {
                o.setCustomOption(keyName, s.customOptions.get(keyName));
            }
        }
        return o;
    }

    @Override
    protected GridLayer createGridLayer(GridLayerOptions o) {
        return TileLayer.create(getState().url, (TileLayerOptions) o);
    }
}
