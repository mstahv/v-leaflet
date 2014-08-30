package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.*;
import org.vaadin.addon.leaflet.shared.EventId;

import com.vividsolutions.jts.geom.Geometry;

public class LTileLayer extends AbstractLeafletLayer {

	public LTileLayer() {
	   	super();
   	   	registerRpc(new LeafletTileLayerServerRpc() {
   	   	   @Override
   	   	   public void onLoading() {
   	   	      fireEvent(new LeafletLoadingEvent(LTileLayer.this));
   	   	   }
	          
   	   	   @Override
   	   	   public void onLoad() {
   	   	      fireEvent(new LeafletLoadEvent(LTileLayer.this));
   	   	   }
   	   	});
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

	public Double getOpacity() {
		return getState().opacity;
	}

	public void setOpacity(Double opacity) {
		getState().opacity = opacity;
	}
	
	public Integer getZindex() {
		return getState().zIndex;
	}
	
	public void setZindex(int zIndex) {
		getState().zIndex = zIndex;
	}

        public Boolean getContinousWorld() {
                return getState().continuousWorld;
        }

        public void setContinuousWorld(Boolean continuousWorld) {
                getState().continuousWorld = continuousWorld;
        }

        public Boolean getNoWrap() {
                return getState().noWrap;
        }

        public void setNoWrap(Boolean noWrap) {
                getState().noWrap = noWrap;
        }

	@Override
	public Geometry getGeometry() {
		return null;
	}
	
	public void addLoadListener(LeafletLoadListener listener) {
		addListener(EventId.LOAD, LeafletLoadEvent.class, listener,
		   	      LeafletLoadListener.METHOD);
	}
	
	public void addLoadingListener(LeafletLoadingListener listener) {
	   	addListener(EventId.LOADING, LeafletLoadingEvent.class, listener,
	   	      LeafletLoadingListener.METHOD);
	}
}
