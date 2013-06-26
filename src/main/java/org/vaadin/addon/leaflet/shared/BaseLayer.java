package org.vaadin.addon.leaflet.shared;

import java.io.Serializable;


public class BaseLayer implements Serializable {
	
	private String url;
	private String name;
	private String attributionString;
	private Boolean detectRetina;
	private Boolean tms;
	private Integer maxZoom;
	private String[] subDomains;
	private Boolean wms;
	private String layers;
	private String styles;
	private String format;
	private Boolean transparent;
	private String version;
	private Double opacity;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAttributionString() {
		return attributionString;
	}
	public void setAttributionString(String attributionString) {
		this.attributionString = attributionString;
	}
	public Boolean getDetectRetina() {
		return detectRetina;
	}
	public void setDetectRetina(Boolean detectRetina) {
		this.detectRetina = detectRetina;
	}
	public void setMaxZoom(Integer maxZoom) {
		this.maxZoom = maxZoom;
	}
	
	public Integer getMaxZoom() {
		return maxZoom;
	}
	
	public void setSubDomains(String... domains) {
		this.subDomains = domains;
	}
	
	public String[] getSubDomains() {
		return subDomains;
	}
	
	@Override
	public String toString() {
		return name != null ? name : "[BaseLayer]";
	}
	
	public Boolean getTms() {
		return tms;
	}
	
	public void setTms(Boolean tms) {
		this.tms = tms;
	}

	public Boolean getWms() {
		return wms;
	}

	public void setWms(Boolean wms) {
		this.wms = wms;
	}

	public String getLayers() {
		return layers;
	}

	public void setLayers(String layers) {
		this.layers = layers;
	}

	public String getStyles() {
		return styles;
	}

	public void setStyles(String styles) {
		this.styles = styles;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Boolean getTransparent() {
		return transparent;
	}

	public void setTransparent(Boolean transparent) {
		this.transparent = transparent;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Double getOpacity() {
		return opacity;
	}

	public void setOpacity(Double opacity) {
		this.opacity = opacity;
	}
}
