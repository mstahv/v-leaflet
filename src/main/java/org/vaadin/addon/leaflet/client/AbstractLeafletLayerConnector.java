package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.AbstractLeafletComponentState;
import org.vaadin.addon.leaflet.shared.MouseOutServerRpc;
import org.vaadin.addon.leaflet.shared.MouseOverServerRpc;
import org.vaadin.addon.leaflet.shared.ClickServerRpc;
import org.vaadin.addon.leaflet.shared.ContextMenuServerRpc;
import org.peimari.gleaflet.client.Layer;
import org.peimari.gleaflet.client.LayerGroup;
import org.peimari.gleaflet.client.Map;
import org.vaadin.addon.leaflet.shared.ILayerClientRpc;

import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import org.vaadin.addon.leaflet.shared.Point;

public abstract class AbstractLeafletLayerConnector<T> extends
        AbstractComponentConnector {

    protected ClickServerRpc rpc = RpcProxy.create(ClickServerRpc.class, this);
    protected MouseOverServerRpc mouseOverRpc = RpcProxy.create(MouseOverServerRpc.class, this);
    protected MouseOutServerRpc mouseOutRpc = RpcProxy.create(MouseOutServerRpc.class, this);
    protected ContextMenuServerRpc contextMenuRpc = RpcProxy.create(ContextMenuServerRpc.class, this);
    private Object leafletParent;

    public AbstractLeafletLayerConnector() {
        super();
        registerRpc(ILayerClientRpc.class, new ILayerClientRpc() {

            /* Doing ugly jsni hack, to avoid limiation in GWT (only one JSO can
             implement interface methods) */
            @Override
            public void bringToFront() {
                jsniBringToFront(getLayer());
            }

            @Override
            public void bringToBack() {
                jsniBringToBack(getLayer());
            }
        });
    }

    private static native final void jsniBringToFront(Layer layer) /*-{
     layer.bringToFront();
     }-*/;

    private static native final void jsniBringToBack(Layer layer) /*-{ 
     layer.bringToBack();
     }-*/;

    private static final Label fakeWidget = new Label();

    @Override
    public Label getWidget() {
        return fakeWidget;
    }

    @Override
    protected void updateWidgetStyleNames() {
        // NOP, not needed, optimized away
    }

    @Override
    protected void updateComponentSize() {
        // NOP, not needed, optimized away
    }

    @Override
    public AbstractLeafletComponentState getState() {
        return (AbstractLeafletComponentState) super.getState();
    }

    public void addToParent(Layer layer) {
        HasComponentsConnector parent = getParent();
        if (parent instanceof LeafletMapConnector) {
            Map map = ((LeafletMapConnector) parent).getMap();
            // Something is wrong if map is null here
            if (getState().active != null && getState().active) {
                map.addLayer(layer);
            }
            leafletParent = map;
        } else {
            LayerGroup layerGroup = (LayerGroup) (((LeafletLayerGroupConnector) parent)
                    .getLayer());
            layerGroup.addLayer(layer);
            leafletParent = layerGroup;
        }
    }

    public void removeLayerFromParent() {
        if (leafletParent == null) {
            VConsole.log("Could not remove layer as layerparent not found, may be timing issue!");
            return;
        }
        Layer layer = getLayer();
        if (leafletParent instanceof Map) {
            Map map = (Map) leafletParent;
            // Something is wrong if map is null here
            map.removeLayer(layer);
        } else {
            ((LayerGroup) leafletParent).removeLayer(layer);
        }
    }

    /**
     * @return the map from the root of the hierarchy
     */
    public Map getMap() {
        LeafletMapConnector leafletMapConnector = getLeafletMapConnector();
        if (leafletMapConnector != null) {
            return leafletMapConnector.getMap();
        }
        return null;
    }

    public LeafletMapConnector getLeafletMapConnector() {
        ServerConnector parent = getParent();
        while (parent != null) {
            if (parent instanceof LeafletMapConnector) {
                return (LeafletMapConnector) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * @return the pixel position of the map
     */
    public Point getMapPixelPosition() {
        final LeafletMapConnector leafletMapConnector = getLeafletMapConnector();
        return leafletMapConnector.getMapPixelPosition();
    }

    @Override
    public HasComponentsConnector getParent() {

        return (HasComponentsConnector) super.getParent();

    }

    private boolean updated = false;

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        if (!stateChangeEvent.isInitialStateChange()) {
            markDirty();
        }
        deferUpdate();
    }

    protected void markDirty() {
        updated = false;
    }

    protected void deferUpdate() {
        LazyUpdator.defer(this);
    }

    protected void updateIfDirty() {
        if (!updated && getParent() != null) {
            update();
            updated = true;
        }
    }

    protected abstract T createOptions();

    protected abstract void update();

    public abstract Layer getLayer();

}
