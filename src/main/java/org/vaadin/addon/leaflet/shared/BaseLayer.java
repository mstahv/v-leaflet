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

}
