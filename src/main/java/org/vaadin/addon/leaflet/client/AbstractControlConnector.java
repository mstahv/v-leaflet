package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.LeafletControlState;
import org.peimari.gleaflet.client.Map;
import org.peimari.gleaflet.client.control.Control;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;

public abstract class AbstractControlConnector<T extends Control> extends
		AbstractExtensionConnector {

	private T control;
	private Map map;

	@Override
	protected void extend(final ServerConnector target) {
		// non tree updates in V7 :(, map might not exist when this is called
		Scheduler.get().scheduleFinally(new ScheduledCommand() {
			@Override
			public void execute() {
				getMap().addControl(getControl());
			}
		});
	}

	protected T getControl() {
		if (control == null) {
			control = createControl();
		}
		return control;
	}

	protected abstract T createControl();

	@Override
	public void onUnregister() {
		super.onUnregister();
		if (getMap() != null) {
			getMap().removeControl(control);
		}
	}

	protected Map getMap() {
		if (map == null) {
			LeafletMapConnector p = (LeafletMapConnector) getParent();
			if (p != null) {
				map = p.getMap();
			}
		}
		return map;
	}

	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		Scheduler.get().scheduleFinally(new ScheduledCommand() {
			@Override
			public void execute() {
				doStateChange(stateChangeEvent);
				if (stateChangeEvent.hasPropertyChanged("position")
						&& getState().position != null) {
					getControl().setPosition(getState().position.toString());
				}
			}
		});
	}

	@Override
	public LeafletControlState getState() {
		return (LeafletControlState) super.getState();
	}

	/**
	 * Deferred state change where layers have been created and added
	 * 
	 * @param stateChangeEvent the event
	 */
	protected void doStateChange(StateChangeEvent stateChangeEvent) {

	}

	@Override
	public LeafletMapConnector getParent() {
		return (LeafletMapConnector) super.getParent();
	}

}
