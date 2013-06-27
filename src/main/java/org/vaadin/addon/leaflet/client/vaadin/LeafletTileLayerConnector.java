package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.raster.TileLayer;

import com.google.gwt.core.client.JsArrayString;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LTileLayer.class)
public class LeafletTileLayerConnector extends
		AbstractLeafletLayerConnector<Options> {

	protected ILayer layer;

	@Override
	public LeafletTileLayerState getState() {
		return (LeafletTileLayerState) super.getState();
	}

	@Override
	protected Options createOptions() {
		Options o = new Options();
		LeafletTileLayerState s = getState();
		o.setProperty("attribution", s.url);
		if (s.detectRetina != null && s.detectRetina) {
			o.setProperty("detectRetina", true);
		}
		if (s.subDomains != null) {
			JsArrayString domain = JsArrayString.createArray().cast();
			for (String a : s.subDomains) {
				domain.push(a);
			}
			o.setProperty("subdomains", (JSObject) domain.cast());
		}
		if (s.maxZoom != null) {
			o.setProperty("maxZoom", s.maxZoom);
		}
		if (s.tms != null && s.tms) {
			o.setProperty("tms", true);
		}
		if (s.opacity != null) {
			o.setProperty("opacity", s.opacity);
		}
		return o;
	}

	@Override
	protected void update() {
		if (layer != null) {
			removeLayerFromParent();
		}
		Options o = createOptions();
		layer = new TileLayer(getState().url, o);
		addToParent(layer);
	}

	@Override
	public ILayer getLayer() {
		return layer;
	}
}
