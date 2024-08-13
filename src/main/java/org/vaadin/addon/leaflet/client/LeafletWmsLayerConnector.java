package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.*;
import org.vaadin.addon.leaflet.shared.LeafletWmsLayerState;
import com.vaadin.shared.ui.Connect;

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
        if (s.crsName != null) {
            o.setCrs(Crs.byName(s.crsName));
        }
		if (s.viewparams != null) {
			o.setViewparams(s.viewparams);
		}
		return o;
	}
	
	@Override
	protected WmsLayer createGridLayer(GridLayerOptions o) {
		if (getState().authToken != null) {
			return WmsLayer.create(getState().url, getState().authToken, getState().layers, (WmsLayerOptions ) o, getMap());
		}
		return WmsLayer.create(getState().url, (WmsLayerOptions ) o);		
	}

}
