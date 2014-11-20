package org.vaadin.addon.leaflet.control;

import org.vaadin.addon.leaflet.shared.LeafletAttributionState;

public class LAttribution extends AbstractDefaultControl {

	@Override
	protected LeafletAttributionState getState() {
		return (LeafletAttributionState) super.getState();
	}

	public void setPrefix(String prefix) {
		getState().prefix = prefix;
	}
		
}
