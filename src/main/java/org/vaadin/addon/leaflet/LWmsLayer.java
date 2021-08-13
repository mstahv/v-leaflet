package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Crs;
import org.vaadin.addon.leaflet.shared.LeafletWmsLayerState;

public class LWmsLayer extends LTileLayer {

    private Crs lCrs;

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

    public Crs getCrs() {
        return lCrs;
    }

    /**
     * Specify the CRS to use for the layer.
     *
     * @param lc The CRS to use (one from a limited range)
     */
    public void setCrs(Crs lc) {
        this.lCrs = lc;
        getState().crsName = lc.getName();
    }

	public void setViewparams(String viewparams) {
	   	getState().viewparams = viewparams;
	}

	public String getViewparams() {
	   	return getState().viewparams;
	}

        public void setCQLFilter(String cqlFilter)
        {
           getState().cqlFilter = cqlFilter;
        }

        public String getCQLFilter()
        {
           return getState().cqlFilter;
        }
}
