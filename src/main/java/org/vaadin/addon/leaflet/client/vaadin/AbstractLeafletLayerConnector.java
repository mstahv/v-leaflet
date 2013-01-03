package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.layers.ILayer;

import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.communication.RpcProxy;
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

	protected abstract T createOptions();
	
	protected abstract void update();

	public abstract ILayer getLayer();
	
}