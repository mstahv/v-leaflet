package org.vaadin.addon.leaflet;


import org.vaadin.addon.leaflet.client.vaadin.AbstractLeafletComponentState;
import org.vaadin.addon.leaflet.client.vaadin.ClickServerRpc;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.AbstractComponent;

public abstract class LeafletLayer extends AbstractComponent {
	
	public LeafletLayer() {
		registerRpc(new ClickServerRpc() {
			@Override
			public void onClick(Point p) {
				fireEvent(new LeafletClickEvent(LeafletLayer.this, p));
			}
		});
	}
	
	@Override
	protected AbstractLeafletComponentState getState() {
		return (AbstractLeafletComponentState) super.getState();
	}
	
	public void addClickListener(LeafletClickListener listener) {
		addListener(LeafletClickEvent.class, listener, LeafletClickListener.METHOD);
	}
	
	public void removeMarkerClickListener(LeafletClickListener listener) {
		removeListener(LeafletClickEvent.class, listener, LeafletClickListener.METHOD);
	}
}
