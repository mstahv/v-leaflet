package org.vaadin.addon.leaflet.client.vaadin;

import org.peimari.gleaflet.client.WmsLayer;
import org.peimari.gleaflet.client.WmsLayerOptions;

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
		return o;
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
