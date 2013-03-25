package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.layers.ILayer;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;

public abstract class AbstractLeafletLayerConnector<T> extends
		AbstractComponentConnector {

	protected ClickServerRpc rpc = RpcProxy.create(ClickServerRpc.class, this);

	public AbstractLeafletLayerConnector() {
		super();
	}

	@Override
	public Label getWidget() {
		// use label as "fake widget"
		return (Label) super.getWidget();
	}

	@Override
	public LeafletMapConnector getParent() {
		return (LeafletMapConnector) super.getParent();
	}

	private boolean updated = true;

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		// state change events are fired in random order. To ensure correct
		// order and only one update (e.g. move of marker), we defer update here
		// and do it only if not forced by parent.
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				if (!updated) {
					update();
				}
				updated = false;
			}
		});
	}

	/**
	 * Should be called by map if update is called by it during hierarchy change
	 * event.
	 */
	public void markUpdated() {
		updated = true;
	}

	protected abstract T createOptions();

	protected abstract void update();

	public abstract ILayer getLayer();

}