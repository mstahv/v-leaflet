package org.vaadin.addon.leaflet.client;


import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.LayerGroup;
import org.peimari.gleaflet.client.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import org.vaadin.addon.leaflet.shared.ILayerClientRpc;

public abstract class AbstractLeafletLayerConnector<T> extends
        AbstractComponentConnector {

    protected ClickServerRpc rpc = RpcProxy.create(ClickServerRpc.class, this);
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
    
    private static native final void jsniBringToFront(ILayer layer)
    /*-{ 
        layer.bringToFront();
    }-*/;

    private static native final void jsniBringToBack(ILayer layer)
    /*-{ 
        layer.bringToBack();
    }-*/;

    @Override
    public Label getWidget() {
        // use label as "fake widget"
        return (Label) super.getWidget();
    }

    @Override
    public AbstractLeafletComponentState getState() {
        return (AbstractLeafletComponentState) super.getState();
    }

    public void addToParent(ILayer layer) {
        HasComponentsConnector parent = getParent();
        if (parent instanceof LeafletMapConnector) {
            Map map = ((LeafletMapConnector) parent).getMap();
            // Something is wrong if map is null here
            if(getState().active != null && getState().active) {
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
        ILayer layer = getLayer();
        if (leafletParent instanceof Map) {
        	Map map = (Map) leafletParent;
            // Something is wrong if map is null here
            map.removeLayer(layer);
        } else {
            ((LayerGroup) leafletParent).removeLayer(layer);
        }
    }

    /** Returns the map from the root of the hierarchy **/
    public Map getMap() {

        ServerConnector parent = getParent();

        while (parent != null) {
            if (parent instanceof LeafletMapConnector) {
                return ((LeafletMapConnector) parent).getMap();
            }

            parent = parent.getParent();
        }

        return null;

    }

    @Override
    public HasComponentsConnector getParent() {

        return (HasComponentsConnector) super.getParent();

    }

    private boolean updated = false;

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        if(!stateChangeEvent.isInitialStateChange()) {
        	markDirty();
        }
        deferUpdate();
    }
    
    protected void markDirty() {
    	updated = false;
    }

    protected void deferUpdate() {
        // state change events are fired in random order. To ensure correct
        // order and only one update (e.g. move of marker), we defer update here
        // and do it only if not forced by parent.
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                updateIfDirty();
            }

        });
    }

    protected void updateIfDirty() {
    	if (!updated) {
    		update();
    		updated = true;
    	}
    }
    
    protected abstract T createOptions();

    protected abstract void update();

    public abstract ILayer getLayer();

}