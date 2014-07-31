package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.AbstractLeafletComponentState;
import org.vaadin.addon.leaflet.client.ClickServerRpc;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.AbstractComponent;
import org.vaadin.addon.leaflet.client.MouseOutServerRpc;
import org.vaadin.addon.leaflet.client.MouseOverServerRpc;
import org.vaadin.addon.leaflet.shared.ILayerClientRpc;

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
        registerRpc(new MouseOverServerRpc() {
            @Override
            public void onMouseOver(Point p) {
                fireEvent(new LeafletMouseOverEvent(AbstractLeafletLayer.this, p));
            }
        });
        registerRpc(new MouseOutServerRpc() {
            @Override
            public void onMouseOut(Point p) {
                fireEvent(new LeafletMouseOutEvent(AbstractLeafletLayer.this, p));
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

    public void addMouseOverListener(LeafletMouseOverListener listener) {
        addListener(LeafletMouseOverEvent.class, listener,
                LeafletMouseOverListener.METHOD);
    }

    public void addMouseOutListener(LeafletMouseOutListener listener) {
        addListener(LeafletMouseOutEvent.class, listener,
                LeafletMouseOutListener.METHOD);
    }

    public void setActive(Boolean active) {
        this.active = active;
        getState().active = active;
    }

    @Override
    public void bringToFront() {
        getRpcProxy(ILayerClientRpc.class).bringToFront();
    }

    @Override
    public void bringToBack() {
        getRpcProxy(ILayerClientRpc.class).bringToBack();
    }

}
