package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.TileLayer;
import org.peimari.gleaflet.client.TileLayerOptions;

import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LTileLayer.class)
public class LeafletTileLayerConnector extends
		AbstractLeafletLayerConnector<TileLayerOptions> {

	protected ILayer layer;

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
		if (s.maxZoom != null) {
			o.setMaxZoom(s.maxZoom);
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
		return o;
	}

	@Override
	protected void update() {
		if (layer != null) {
			removeLayerFromParent();
		} else {
			TileLayerOptions o = createOptions();
			layer = TileLayer.create(getState().url, o);
		}
		
		addToParent(layer);
	}

	@Override
	public ILayer getLayer() {
		return layer;
	}
}
