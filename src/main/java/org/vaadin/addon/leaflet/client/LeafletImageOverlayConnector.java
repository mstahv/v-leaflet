package org.vaadin.addon.leaflet.client;

import com.vaadin.client.MouseEventDetailsBuilder;
import org.vaadin.addon.leaflet.shared.DragEndServerRpc;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.MouseEvent;

import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.ImageOverlay;
import org.peimari.gleaflet.client.ImageOverlayOptions;
import org.peimari.gleaflet.client.LatLngBounds;
import org.peimari.gleaflet.client.Layer;
import org.vaadin.addon.leaflet.shared.LeafletImageOverlayState;

@Connect(org.vaadin.addon.leaflet.LImageOverlay.class)
public class LeafletImageOverlayConnector extends
		AbstractLeafletLayerConnector<ImageOverlayOptions> {

	private ImageOverlay imageOverlay;

	DragEndServerRpc dragServerRcp = getRpcProxy(DragEndServerRpc.class);

	@Override
	public LeafletImageOverlayState getState() {
		return (LeafletImageOverlayState) super.getState();
	}

	ClickListener handler = new ClickListener() {
		@Override
		public void onClick(MouseEvent event) {
                rpc.onClick(U.toPoint(event.getLatLng()),
                        MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), getLeafletMapConnector().getWidget().getElement()));
		}
	};

	@Override
	protected void update() {
		if (imageOverlay != null) {
			removeLayerFromParent();
		}
        
        LatLngBounds bounds = LatLngBounds.create(LatLng.create(getState().bounds.getSouthWestLat(), 
                getState().bounds.getSouthWestLon()),
                LatLng.create(getState().bounds.getNorthEastLat(), 
                        getState().bounds.getNorthEastLon()));
        
		URLReference urlReference = getState().resources.get("url");

        ImageOverlayOptions options = createOptions();
        if(getState().attribution != null) {
            options.setAttribution(getState().attribution);
        }
        if(getState().opacity != null) {
            options.setOpacity(getState().opacity);
        }
                
        String url = getConnection().translateVaadinUri(urlReference.getURL());
		imageOverlay = ImageOverlay.create(url, bounds, options);
		addToParent(imageOverlay);
        
	}

	@Override
	protected ImageOverlayOptions createOptions() {
		ImageOverlayOptions o = ImageOverlayOptions.create();
		return o;
	}

	@Override
	public Layer getLayer() {
		return imageOverlay;
	}

}
