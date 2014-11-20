package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.LeafletAttributionState;
import org.peimari.gleaflet.client.control.Attribution;
import org.vaadin.addon.leaflet.control.LAttribution;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(LAttribution.class)
public class LeafletAttributionConnector<Atribution> extends AbstractDefaultControl {

	@Override
	protected Attribution createControl() {
		return getMap().getAttributionControl();
	}
	
	@Override
	public LeafletAttributionState getState() {
		return (LeafletAttributionState) super.getState();
	}
	
	@Override
	protected Attribution getControl() {
		return (Attribution) super.getControl();
	}
	
	@Override
	protected void doStateChange(StateChangeEvent stateChangeEvent) {
		super.doStateChange(stateChangeEvent);
		getControl().setPrefix(getState().prefix);
	}

}
