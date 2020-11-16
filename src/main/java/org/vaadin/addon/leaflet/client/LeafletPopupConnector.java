package org.vaadin.addon.leaflet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import org.peimari.gleaflet.client.*;
import org.vaadin.addon.leaflet.shared.LeafletPopupState;
import org.vaadin.addon.leaflet.shared.PopupServerRpc;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import org.vaadin.addon.leaflet.shared.ClickServerRpc;
import org.vaadin.addon.leaflet.shared.PopupState;

@Connect(org.vaadin.addon.leaflet.LPopup.class)
public class LeafletPopupConnector extends
        AbstractComponentConnector {
    
    static private Label fakeWidget = new Label();

    private Popup popup;

    PopupServerRpc serverRpc = getRpcProxy(PopupServerRpc.class);
    
    ClickServerRpc clickRpc = getRpcProxy(ClickServerRpc.class);

	private Map map;
    private JavaScriptObject closeListener;
    
    public LeafletPopupConnector() {
    }

    @Override
    public LeafletPopupState getState() {
        return (LeafletPopupState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                if (popup != null) {
                    if (closeListener != null) {
                        popup.removeListener(closeListener);
                        closeListener = null;
                    }
                    getMap().closePopup(popup);
                }
                LatLng latlng = LatLng.create(getState().point.getLat(),
                        getState().point.getLon());

                PopupOptions options = popupOptionsFor(getState().popupState, LeafletPopupConnector.this);
                popup = (Popup) Popup.create().create(options).setLatLng(latlng);
                popup.setContent(getState().htmlContent);
                if (getState().popupState.closeButton || getState().popupState.closeOnClick) {
                    // Non closeble are closed from server side, no need for
                    // event
                    closeListener = popup.addLayerRemovedListener(new LayerRemovedListener() {
                        @Override
                        public void onRemoved(Event event) {
                            serverRpc.closed();
                            popup = null;
                        }
                    });
                }
                popup.addTo(getMap());

                Element content = popup.getContentNode();
                DOM.sinkEvents(content, com.google.gwt.user.client.Event.ONCLICK);
                DOM.setEventListener(content, new EventListener() {
                    @Override
                    public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                        clickRpc.onClick(getState().point, MouseEventDetailsBuilder.buildMouseEventDetails(event, getLeafletMapConnector().getWidget().getElement()));
                    }
                });

            }
        });

    }

    public static PopupOptions popupOptionsFor(PopupState popupState, AbstractComponentConnector c) {
        if (popupState == null) {
            return null;
        }
        PopupOptions popupOptions = PopupOptions.create();
        if (popupState.maxWidth > 0) {
            popupOptions.setMaxWidth(popupState.maxWidth);
        }
        if (popupState.minWidth > 0) {
            popupOptions.setMinWidth(popupState.minWidth);
        }
        if (popupState.maxHeight > 0) {
            popupOptions.setMaxHeight(popupState.maxHeight);
        }
        popupOptions.setAutoPan(popupState.autoPan);
        popupOptions.setCloseButton(popupState.closeButton);
        popupOptions.setCloseOnClick(popupState.closeOnClick);
        popupOptions.setAutoClose(popupState.autoClose);
        popupOptions.setKeepInView(popupState.keepInView);
        String styleName = StyleUtil.getStyleNameFromComponentState(c.getState());
        popupOptions.setClassName(styleName);
        if (popupState.offset != null) {
            popupOptions.setOffset(Point.create(popupState.offset.getLat(),
                    popupState.offset.getLon()));
        }
        popupOptions.setZoomAnimation(popupState.zoomAnimation);
        if (popupState.autoPanPadding != null) {
            popupOptions.setAutoPanPadding(Point.create(
                    popupState.autoPanPadding.getLat(),
                    popupState.autoPanPadding.getLon()));
        }
        return popupOptions;
    }

    /**
     * @return the map from the root of the hierarchy *
     */
    public Map getMap() {
    	if(map == null) {
            ServerConnector parent = getParent();
            while (parent != null) {
                if (parent instanceof LeafletMapConnector) {
                    map = ((LeafletMapConnector) parent).getMap();
                }
                parent = parent.getParent();
            }
    	}

        return map;
    }

    @Override
    public Label getWidget() {
        // use label as "fake widget"
        return fakeWidget;
    }

    @Override
    public void onUnregister() {
        if(popup != null) {
            try {
                getMap().closePopup(popup);
            } catch(Exception e) {
                VConsole.log("Popup already closed? " + e.getMessage());
            }
        }
        super.onUnregister();
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

}
