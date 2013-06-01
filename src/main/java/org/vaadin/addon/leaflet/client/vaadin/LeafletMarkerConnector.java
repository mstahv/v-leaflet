package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.marker.Marker;
import org.discotools.gwt.leaflet.client.marker.MarkerOptions;
import org.discotools.gwt.leaflet.client.popup.PopupOptions;
import org.discotools.gwt.leaflet.client.types.DivIcon;
import org.discotools.gwt.leaflet.client.types.DivIconOptions;
import org.discotools.gwt.leaflet.client.types.Icon;
import org.discotools.gwt.leaflet.client.types.IconOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.discotools.gwt.leaflet.client.types.Point;

import com.vaadin.client.VConsole;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LMarker.class)
public class LeafletMarkerConnector extends
        AbstractLeafletLayerConnector<MarkerOptions> {

    private Marker marker;

	LeafletMarkerClientRpc clientRpc = new LeafletMarkerClientRpc() {
		
		@Override
		public void openPopup() {
			marker.openPopup();
		}
		
		@Override
		public void closePopup() {
			marker.closePopup();
		}
	};
	
	public LeafletMarkerConnector() {
		registerRpc(LeafletMarkerClientRpc.class, clientRpc);
	}
    @Override
    public LeafletMarkerState getState() {
        return (LeafletMarkerState) super.getState();
    }

    EventHandler<?> handler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            rpc.onClick(null);
        }
    };

    @Override
    protected void update() {
        VConsole.error("update" + getConnectorId());
        if (marker != null) {
            removeLayerFromParent();
            EventHandlerManager.clearEventHandler(marker, Events.click);
        }
        LatLng latlng = new LatLng(getState().point.getLat(),
                getState().point.getLon());
        MarkerOptions options = createOptions();

        URLReference urlReference = getState().resources.get("icon");
        String divIcon = getState().divIcon;
        if (divIcon != null) {
        	DivIconOptions divIconOptions = new DivIconOptions();
        	if (getState().iconAnchor != null) {
                divIconOptions.setIconAnchor(new Point(getState().iconAnchor
                        .getLat(), getState().iconAnchor.getLon()));
            }
            if (getState().iconSize != null) {
                divIconOptions.setIconSize(new Point(getState().iconSize.getLat(),
                        getState().iconSize.getLon()));
            }
            divIconOptions.setHtml(divIcon);
            DivIcon icon = new DivIcon(divIconOptions);
            options.setIcon(icon);
        } else if (urlReference != null) {
            IconOptions iconOptions = new IconOptions();
            iconOptions.setIconUrl(urlReference.getURL());
            if (getState().iconAnchor != null) {
                iconOptions.setIconAnchor(new Point(getState().iconAnchor
                        .getLat(), getState().iconAnchor.getLon()));
            }
            if (getState().iconSize != null) {
                iconOptions.setIconSize(new Point(getState().iconSize.getLat(),
                        getState().iconSize.getLon()));
            }
            Icon icon = new Icon(iconOptions);
            options.setIcon(icon);
        }
        
        String title = getState().title;
        if(title != null){
        	options.setTitle(title);
        }
        marker = new Marker(latlng, options);
        String popup = getState().popup;
        if(popup != null){
        	PopupState popupState = getState().popupState;
        	if(popupState != null){
        		PopupOptions popupOptions = new PopupOptions();
        		popupOptions.setMaxWidth(popupState.maxWidth);
        		popupOptions.setMinWidth(popupState.minWidth);
        		popupOptions.setProperty("autoPan", popupState.autoPan);
        		popupOptions.setProperty("closeButton", popupState.closeButton);
        		popupOptions.setProperty("offset", popupState.offset);
        		popupOptions.setProperty("zoomAnimation", popupState.zoomAnimation);
        		popupOptions.setProperty("autoPanPadding", popupState.autoPanPadding);
        		marker.bindPopup(popup, popupOptions);
        	} else {
        		marker.bindPopup(popup);
        	}
        }
        addToParent(marker);

        EventHandlerManager.addEventHandler(marker, Events.click, handler);
    }

    @Override
    protected MarkerOptions createOptions() {
        MarkerOptions markerOptions = new MarkerOptions();
        return markerOptions;
    }

    @Override
    public ILayer getLayer() {
        return marker;
    }

}
