package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.control.Control;

import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;

public abstract class AbstractDefaultControl<T extends Control> extends
		AbstractControlConnector {

	private boolean removed;

	public AbstractDefaultControl() {
		super();
	}

	@Override
	protected void extend(ServerConnector target) {
		// NOP, skip reattaching default control
	}

	protected void doStateChange(StateChangeEvent stateChangeEvent) {
		if(!getState().enabled) {
			getMap().removeControl(getControl());
			removed = true;
		} else if(removed) {
			getMap().addControl(getControl());
			removed = false;
		}
	}

}