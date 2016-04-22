package org.vaadin.addon.leaflet.control;

import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.shared.LeafletLayersState;
import org.vaadin.addon.leaflet.shared.LayerControlInfo;


public class LLayers extends AbstractControl {

	public LLayers() {
		super();
	}
	public LLayers(LLayers lLayers) {
		super();
		LeafletLayersState state = getState();
		state.collapsed = lLayers.getState().collapsed;
		state.layerContolInfo = lLayers.getState().layerContolInfo;
	}

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

    public void setCollapsed(Boolean collapsed) {
        getState().collapsed = collapsed;
    }

}
