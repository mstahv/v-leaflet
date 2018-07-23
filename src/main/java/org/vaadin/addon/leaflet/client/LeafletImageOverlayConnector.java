package org.vaadin.addon.leaflet.client;

import com.google.gwt.core.client.Scheduler;
import com.vaadin.client.MouseEventDetailsBuilder;
import org.peimari.gleaflet.client.*;
import org.vaadin.addon.leaflet.shared.DragEndServerRpc;

import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;
import org.vaadin.addon.leaflet.shared.EventId;
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
			imageOverlay.removeClickListener();
			imageOverlay.removeContextMenuListener();
			imageOverlay.removeMouseOverListener();
			imageOverlay.removeMouseOutListener();
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
        if (getState().alt != null) {
			options.setAlt(getState().alt);
		}
		if (getState().interactive != null) {
			options.setInteractive(getState().interactive);
		}
		if (getState().zIndex != null) {
			options.setZIndex(getState().zIndex);
		}

        String url = getConnection().translateVaadinUri(urlReference.getURL());
		imageOverlay = ImageOverlay.create(url, bounds, options);
		addToParent(imageOverlay);

		imageOverlay.addClickListener(handler);
		if (hasEventListener(EventId.CONTEXTMENU)) {
			imageOverlay.addContextMenuListener(new ContextMenuListener() {
				@Override
				public void onContextMenu(MouseEvent event) {
					contextMenuRpc.onContextMenu(U.toPoint(event.getLatLng()),
							MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), getLeafletMapConnector().getWidget().getElement()));
				}
			});
		}
		if (hasEventListener(EventId.MOUSEOVER)) {
            /*
             * Add listener lazily to avoid extra event if layer is modified in
             * server side listener. This can be removed if "clear and rebuild"
             * style component updates are changed into something more
             * intelligent at some point.
             */
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					imageOverlay.addMouseOverListener(new MouseOverListener() {
						@Override
						public void onMouseOver(MouseEvent event) {
							mouseOverRpc.onMouseOver(U.toPoint(event.getLatLng()));
						}
					});
				}
			});
		}
		if (hasEventListener(EventId.MOUSEOUT)) {
			imageOverlay.addMouseOutListener(new MouseOutListener() {
				@Override
				public void onMouseOut(MouseEvent event) {
					mouseOutRpc.onMouseOut(U.toPoint(event.getLatLng()));
				}
			});
		}
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
