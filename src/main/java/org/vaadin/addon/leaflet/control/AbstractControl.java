package org.vaadin.addon.leaflet.control;

import org.vaadin.addon.leaflet.shared.LeafletControlState;
import org.vaadin.addon.leaflet.shared.ControlPosition;

import com.vaadin.server.AbstractExtension;

public abstract class AbstractControl extends AbstractExtension {

	public AbstractControl() {
		super();
	}
	
	@Override
	protected LeafletControlState getState() {
		return (LeafletControlState) super.getState();
	}

	public void setPosition(ControlPosition position) {
		getState().position = position;
	}

	public ControlPosition getPosition() {
		return getState().position;
	}

}