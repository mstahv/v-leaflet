package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.*;

import com.vaadin.client.communication.*;
import com.vaadin.shared.ui.*;

@Connect(org.vaadin.addon.leaflet.LTileLayer.class)
public class LeafletTileLayerConnector extends
		AbstractLeafletLayerConnector<TileLayerOptions> {

	protected ILayer layer;
	protected LeafletTileLayerServerRpc tileLayerServerRpc = RpcProxy.create(LeafletTileLayerServerRpc.class, this);
	
	@Override
	public LeafletTileLayerState getState() {
		return (LeafletTileLayerState) super.getState();
	}

	@Override
	protected TileLayerOptions createOptions() {
		TileLayerOptions o = TileLayerOptions.create();
		LeafletTileLayerState s = getState();
		o.setAttribution(s.attributionString);
		if (s.detectRetina != null && s.detectRetina) {
			o.setDetectRetina(true);
		}
		if (s.subDomains != null) {
			o.setSubDomains(s.subDomains);
		}
		if (s.maxZoom != null) {
			o.setMaxZoom(s.maxZoom);
		}
		if (s.tms != null && s.tms) {
			o.setTms(true);
		}
		if (s.opacity != null) {
			o.setOpacity(s.opacity);
		}
		if (s.zIndex != null) {
			o.setZindex(s.zIndex);
		}
                if (s.continuousWorld != null) {
                    o.setContinuousWorld(s.continuousWorld);
                }
                if (s.noWrap != null) {
                    o.setNoWrap(s.noWrap);
                }
		return o;
	}

	@Override
	protected void update() {
		if (layer != null) {
			removeLayerFromParent();
		} else {
			TileLayerOptions o = createOptions();
			layer = TileLayer.create(getState().url, o);
		}
		TileLayer tileLayer = (TileLayer) layer;
		if (hasEventListener("load")) {
		   	tileLayer.addLoadListener(new LoadListener() {
		   	   @Override
		   	   public void onLoad(Event event)
		   	   {
		   	      tileLayerServerRpc.onLoad();
		   	   }
			});
		}
		if (hasEventListener("loading")) {
		   	tileLayer.addLoadingListener(new LoadingListener() {
		   	   @Override
		   	   public void onLoading(Event event)
		   	   {
		   	      tileLayerServerRpc.onLoading();
		   	   }
			});
		}
		addToParent(layer);
	}

	@Override
	public ILayer getLayer() {
		return layer;
	}
}
