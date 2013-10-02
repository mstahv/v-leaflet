package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.LeafletWmsLayerState;

public class LWmsLayer extends LTileLayer {

	@Override
	protected LeafletWmsLayerState getState() {
		return (LeafletWmsLayerState) super.getState();
	}

	public String getLayers() {
		return getState().layers;
	}

	public void setLayers(String layers) {
		getState().layers = layers;
	}

	public String getStyles() {
		return getState().layerStyles;
	}

	public void setStyles(String styles) {
		getState().layerStyles = styles;
	}

	public String getFormat() {
		return getState().format;
	}

	public void setFormat(String format) {
		getState().format = format;
	}

	public Boolean getTransparent() {
		return getState().transparent;
	}

	public void setTransparent(Boolean transparent) {
		getState().transparent = transparent;
	}

	public String getVersion() {
		return getState().version;
	}

	public void setVersion(String version) {
		getState().version = version;
	}
}
