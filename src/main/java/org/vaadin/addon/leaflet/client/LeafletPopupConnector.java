package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.LeafletPopupState;
import org.vaadin.addon.leaflet.shared.PopupServerRpc;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.Event;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.Map;
import org.peimari.gleaflet.client.Popup;
import org.peimari.gleaflet.client.PopupClosedListener;
import org.peimari.gleaflet.client.PopupOptions;

@Connect(org.vaadin.addon.leaflet.LPopup.class)
public class LeafletPopupConnector extends
        AbstractComponentConnector {
    
    static private Label fakeWidget = new Label();

    private Popup popup;

    PopupServerRpc serverRpc = getRpcProxy(PopupServerRpc.class);

	private Map map;

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
                    getMap().closePopup(popup);
                }
                LatLng latlng = LatLng.create(getState().point.getLat(),
                        getState().point.getLon());
                
                PopupOptions options = PopupOptions.create();
                options.setCloseButton(getState().closeButton);
                options.setCloseOnClick(getState().closeOnClick);
                popup = Popup.create().create(options).setLatLng(latlng);
                popup.setContent(getState().htmlContent);
                if(getState().closeButton || getState().closeOnClick) {
                    // Non closeble are closed from server side, no need for
                    // event
                    popup.addCloseListener(new PopupClosedListener() {

                        @Override
                        public void onClosed(Event event) {
                            serverRpc.closed();
                            popup = null;
                        }
                    });
                }
                popup.addTo(getMap());

            }
        });

    }

    /**
     * Returns the map from the root of the hierarchy *
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

}
