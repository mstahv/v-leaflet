package org.vaadin.addon.leaflet.shared;

import java.util.Map;

public class LeafletTileLayerState extends AbstractLeafletComponentState {

	public String url;
	public String attributionString;
	public Boolean detectRetina;
	public Boolean tms;
	public Integer minZoom;
	public Integer maxZoom;
	public String[] subDomains;
	public Double opacity;
	public Integer zIndex;
        public Boolean continuousWorld;
        public Boolean noWrap;
        public Map<String,String> customOptions;
}
