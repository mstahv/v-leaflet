package org.vaadin.addon.leaflet.control;

import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.client.LeafletLayersState;
import org.vaadin.addon.leaflet.shared.LayerControlInfo;

import com.vaadin.server.AbstractExtension;

public class LLayers extends AbstractExtension {

	public void addOverlay(LeafletLayer overlay, String name) {
		LayerControlInfo info = new LayerControlInfo();
		info.baseLayer = false;
		info.name = name;
		getState().layerContolInfo.put(overlay, info);
	}
	
	@Override
	protected LeafletLayersState getState() {
		return (LeafletLayersState) super.getState();
	}

	public void addBaseLayer(LeafletLayer baseLayer, String name) {
		LayerControlInfo info = new LayerControlInfo();
		info.baseLayer = true;
		info.name = name;
		getState().layerContolInfo.put(baseLayer, info);
	}

	public void removeLayer(LeafletLayer c) {
		getState().layerContolInfo.remove(c);
	}

}
