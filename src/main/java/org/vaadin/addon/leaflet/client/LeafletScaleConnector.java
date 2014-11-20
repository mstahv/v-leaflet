package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.LeafletScaleState;
import org.peimari.gleaflet.client.control.Scale;
import org.peimari.gleaflet.client.control.ScaleOptions;
import org.vaadin.addon.leaflet.control.LScale;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(LScale.class)
public class LeafletScaleConnector extends AbstractControlConnector<Scale> {

	@Override
	protected Scale createControl() {
		ScaleOptions o = ScaleOptions.create();
		if(getState().imperial != null) {
			boolean booleanValue = getState().imperial.booleanValue();
			o.setImperial(booleanValue);
		}
		if(getState().metric != null) {
			boolean booleanValue = getState().metric.booleanValue();
			o.setMetric(booleanValue);
		}
		return Scale.create(o);
	}

	protected void doStateChange(StateChangeEvent stateChangeEvent) {
		super.doStateChange(stateChangeEvent);
	}

	@Override
	public LeafletScaleState getState() {
		return (LeafletScaleState) super.getState();
	}

}
