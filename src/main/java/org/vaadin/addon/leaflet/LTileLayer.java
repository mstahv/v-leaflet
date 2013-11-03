package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.LeafletTileLayerState;

public class LTileLayer extends AbstractLeafletLayer {

	public LTileLayer() {
	}

	public LTileLayer(String url) {
		setUrl(url);
	}

	@Override
	protected LeafletTileLayerState getState() {
		return (LeafletTileLayerState) super.getState();
	}

	public String getUrl() {
		return getState().url;
	}

	public void setUrl(String url) {
		getState().url = url;
	}

	public String getAttributionString() {
		return getState().attributionString;
	}

	public void setAttributionString(String attributionString) {
		getState().attributionString = attributionString;
	}

	public Boolean getDetectRetina() {
		return getState().detectRetina;
	}

	public void setDetectRetina(Boolean detectRetina) {
		getState().detectRetina = detectRetina;
	}

	public Boolean getTms() {
		return getState().tms;
	}

	public void setTms(Boolean tms) {
		getState().tms = tms;
	}

	public Integer getMaxZoom() {
		return getState().maxZoom;
	}

	public void setMaxZoom(Integer maxZoom) {
		getState().maxZoom = maxZoom;
	}

	public String[] getSubDomains() {
		return getState().subDomains;
	}

	public void setSubDomains(String... string) {
		getState().subDomains = string;
	}

	public Double getOpacity() {
		return getState().opacity;
	}

	public void setOpacity(Double opacity) {
		getState().opacity = opacity;
	}
}
