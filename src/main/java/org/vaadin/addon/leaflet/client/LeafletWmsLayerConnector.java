package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.WmsLayer;
import org.peimari.gleaflet.client.WmsLayerOptions;

import com.vaadin.shared.ui.Connect;
import org.vaadin.addon.leaflet.shared.Crs;

@Connect(org.vaadin.addon.leaflet.LWmsLayer.class)
public class LeafletWmsLayerConnector extends LeafletTileLayerConnector {

	@Override
	public LeafletWmsLayerState getState() {
		return (LeafletWmsLayerState) super.getState();
	}

	@Override
	protected WmsLayerOptions createOptions() {
		WmsLayerOptions o = super.createOptions().cast();
		LeafletWmsLayerState s = getState();
		if (s.layers != null) {
			o.setLayers(s.layers);
		}
		if (s.layerStyles != null) {
			o.setStyles(s.layerStyles);
		}
		if (s.format != null) {
			o.setFormat(s.format);
		}
		if (s.transparent != null && s.transparent) {
			o.setTransparent(true);
		}
		if (s.version != null) {
			o.setVersion(s.version);
		}
        if (s.crsId != null) {
            o.setCrs(mapCrs(s.crsId));
        }
		return o;
	}

    private org.peimari.gleaflet.client.Crs mapCrs(String crsId) {
        if (Crs.EPSG4326.getId().equals(crsId)) {
            return org.peimari.gleaflet.client.Crs.EPSG4326();
        } else if (Crs.EPSG3857.getId().equals(crsId)) {
            return (org.peimari.gleaflet.client.Crs.EPSG3857());
        } else if (Crs.EPSG3395.getId().equals(crsId)) {
            return (org.peimari.gleaflet.client.Crs.EPSG3395());
        } else if (Crs.SIMPLE.getId().equals(crsId)) {
            return (org.peimari.gleaflet.client.Crs.Simple());
        } else {
            return (org.peimari.gleaflet.client.Crs.EPSG3857());
        }

    }

	@Override
	protected void update() {
		if (layer != null) {
			removeLayerFromParent();
		}
		WmsLayerOptions o = createOptions();
		layer = WmsLayer.create(getState().url, o);
		addToParent(layer);
	}
}
