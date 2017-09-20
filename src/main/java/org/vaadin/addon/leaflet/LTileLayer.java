package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.LeafletTileLayerState;
import java.util.HashMap;
import java.util.Map;

public class LTileLayer extends LGridLayer {

    public LTileLayer() {
        super();
    }

    public LTileLayer(String url) {
        this();
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

    public Integer getMinZoom() {
        return getState().minZoom;
    }

    public void setMinZoom(Integer minZoom) {
        getState().minZoom = minZoom;
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

    public Map<String, String> getCustomOptions() {
        return getState().customOptions;
    }

    public void setCustomOptions(Map<String, String> customOptions) {
        getState().customOptions = customOptions;
    }

    public void setCustomOption(String optionName, String optionValue) {
        if (getState().customOptions == null) {
            getState().customOptions = new HashMap<String, String>();
        }
        getState().customOptions.put(optionName, optionValue);
    }
}
