package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.AbstractLeafletComponentState;
import org.vaadin.addon.leaflet.client.ClickServerRpc;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.AbstractComponent;

public abstract class AbstractLeafletLayer extends AbstractComponent implements
		LeafletLayer {

	private Boolean active = true;

	public AbstractLeafletLayer() {
		registerRpc(new ClickServerRpc() {
			@Override
			public void onClick(Point p) {
				fireEvent(new LeafletClickEvent(AbstractLeafletLayer.this, p));
			}
		});
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		super.beforeClientResponse(initial);
		getState().active = active;
	}

	@Override
	protected AbstractLeafletComponentState getState() {
		return (AbstractLeafletComponentState) super.getState();
	}

	public void addClickListener(LeafletClickListener listener) {
		addListener(LeafletClickEvent.class, listener,
				LeafletClickListener.METHOD);
	}

	public void setActive(Boolean active) {
		this.active = active;
		getState().active = active;
	}
}
