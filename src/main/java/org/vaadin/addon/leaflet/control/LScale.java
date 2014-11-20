package org.vaadin.addon.leaflet.control;

import org.vaadin.addon.leaflet.shared.LeafletScaleState;

public class LScale extends AbstractControl {

	@Override
	protected LeafletScaleState getState() {
		return (LeafletScaleState) super.getState();
	}

	public void setImperial(boolean showImperial) {
		getState().imperial = showImperial;
	}
	
	public void setMetric(boolean showMetric) {
		getState().metric = showMetric;
	}

}
