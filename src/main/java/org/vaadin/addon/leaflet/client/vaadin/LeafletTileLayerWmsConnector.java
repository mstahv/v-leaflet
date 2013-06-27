package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.layers.raster.WmsLayer;

import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LTileLayerWms.class)
public class LeafletTileLayerWmsConnector extends LeafletTileLayerConnector {

	@Override
	public LeafletTileLayerWmsState getState() {
		return (LeafletTileLayerWmsState) super.getState();
	}

	@Override
	protected Options createOptions() {
		Options o = super.createOptions();
		LeafletTileLayerWmsState s = getState();
		if (s.layers != null) {
			o.setProperty("layers", s.layers);
		}
		if (s.layerStyles != null) {
			o.setProperty("styles", s.layerStyles);
		}
		if (s.format != null) {
			o.setProperty("format", s.format);
		}
		if (s.transparent != null && s.transparent) {
			o.setProperty("transparent", true);
		}
		if (s.version != null) {
			o.setProperty("version", s.version);
		}
		return o;
	}

	@Override
	protected void update() {
		if (layer != null) {
			removeLayerFromParent();
		}
		Options o = createOptions();
		layer = new WmsLayer(getState().url, o);
		addToParent(layer);
	}
}
