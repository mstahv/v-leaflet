package org.vaadin.addon.leaflet.shared;

import java.util.Map;

public class LeafletTileLayerState extends LeafletGridLayerState {

    public String url;
    public Boolean detectRetina;
    public Boolean tms;
    public Integer minZoom;
    public Integer maxZoom;
    public String[] subDomains;
    public Map<String, String> customOptions;
    
}
