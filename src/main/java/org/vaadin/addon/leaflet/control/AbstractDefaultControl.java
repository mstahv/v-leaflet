package org.vaadin.addon.leaflet.control;

public abstract class AbstractDefaultControl extends AbstractControl {

	public void setEnabled(boolean enabled) {
		getState().enabled = enabled;
	}
	
	public boolean isEnabled() {
		return getState(false).enabled;
	}

}