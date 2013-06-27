package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.AbstractLeafletComponentState;
import org.vaadin.addon.leaflet.client.vaadin.ClickServerRpc;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.AbstractComponent;

public abstract class LeafletLayer extends AbstractComponent {

    private String name;
    private Boolean active;

    public LeafletLayer(String name) {
		this(name, true);
    }

    public LeafletLayer(String name, Boolean active) {
        // the idea of accessing the state from constructor may not be
        // a good idea(?)
        this.name = name;
        this.active = active;
        registerRpc(new ClickServerRpc() {
            @Override
            public void onClick(Point p) {
                fireEvent(new LeafletClickEvent(LeafletLayer.this, p));
            }
        });
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().name = name;
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

    public void removeMarkerClickListener(LeafletClickListener listener) {
        removeListener(LeafletClickEvent.class, listener,
                LeafletClickListener.METHOD);
    }

    public void setActive(Boolean active) {
		this.active = active;
		getState().active = active;
    }
}
