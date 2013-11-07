package org.vaadin.addon.leaflet.client.vaadin;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.DivIcon;
import org.peimari.gleaflet.client.DivIconOptions;
import org.peimari.gleaflet.client.ILayer;
import org.peimari.gleaflet.client.Icon;
import org.peimari.gleaflet.client.IconOptions;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.Marker;
import org.peimari.gleaflet.client.MarkerOptions;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.Point;
import org.peimari.gleaflet.client.PopupOptions;

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

	DragEndServerRpc dragServerRcp = getRpcProxy(DragEndServerRpc.class);
	
	public LeafletMarkerConnector() {
		registerRpc(LeafletMarkerClientRpc.class, clientRpc);
	}

	@Override
	public LeafletMarkerState getState() {
		return (LeafletMarkerState) super.getState();
	}

	ClickListener handler = new ClickListener() {
		@Override
		public void onClick(MouseEvent event) {
			rpc.onClick(null);
		}
	};

	@Override
	protected void update() {
		VConsole.error("update" + getConnectorId());
		if (marker != null) {
			removeLayerFromParent();
			marker.removeClickListener();
		}
		LatLng latlng = LatLng.create(getState().point.getLat(),
				getState().point.getLon());
		MarkerOptions options = createOptions();

		URLReference urlReference = getState().resources.get("icon");
		String divIcon = getState().divIcon;
		if (divIcon != null) {
			DivIconOptions divIconOptions = DivIconOptions.create();
			if (getState().iconAnchor != null) {
				divIconOptions.setIconAnchor(Point.create(
						getState().iconAnchor.getLat(),
						getState().iconAnchor.getLon()));
			}
			if (getState().iconSize != null) {
				divIconOptions.setIconSize(Point.create(
						getState().iconSize.getLat(),
						getState().iconSize.getLon()));
			}
			divIconOptions.setHtml(divIcon);
			DivIcon icon = DivIcon.create(divIconOptions);
			options.setIcon(icon);
		} else if (urlReference != null) {
			IconOptions iconOptions = IconOptions.create();
			iconOptions.setIconUrl(urlReference.getURL());
			if (getState().iconAnchor != null) {
				iconOptions.setIconAnchor(Point.create(
						getState().iconAnchor.getLat(),
						getState().iconAnchor.getLon()));
			}
			if (getState().iconSize != null) {
				iconOptions.setIconSize(Point.create(
						getState().iconSize.getLat(),
						getState().iconSize.getLon()));
			}
			Icon icon = Icon.create(iconOptions);
			options.setIcon(icon);
		}

		String title = getState().title;
		if (title != null) {
			options.setTitle(title);
		}

		if (hasEventListener("dragend")) {
			options.setDraggable(true);
		}

		marker = Marker.create(latlng, options);
		if (hasEventListener("dragend")) {
			marker.addDragEndListener(new ClickListener() {
				@Override
				public void onClick(MouseEvent event) {
					dragServerRcp.dragEnd(U.toPoint(marker.getLatLng()));
				}
			});
		}
		String popup = getState().popup;
		if (popup != null) {
			PopupState popupState = getState().popupState;
			if (popupState != null) {
				PopupOptions popupOptions = PopupOptions.create();
				popupOptions.setMaxWidth(popupState.maxWidth);
				popupOptions.setMinWidth(popupState.minWidth);
				popupOptions.setAutoPan(popupState.autoPan);
				popupOptions.setCloseButton(popupState.closeButton);
				if (popupState.offset != null) {
					popupOptions.setOffset(Point.create(
							popupState.offset.getLat(),
							popupState.offset.getLon()));
				}
				popupOptions.setZoomAnimation(popupState.zoomAnimation);
				if (popupState.autoPanPadding != null) {
					popupOptions.setAutoPanPadding(Point.create(
							popupState.autoPanPadding.getLat(),
							popupState.autoPanPadding.getLon()));
				}
				marker.bindPopup(popup, popupOptions);
			} else {
				marker.bindPopup(popup);
			}
		}
		addToParent(marker);

		marker.addClickListener(handler);
	}

	@Override
	protected MarkerOptions createOptions() {
		MarkerOptions markerOptions = MarkerOptions.create();
		return markerOptions;
	}

	@Override
	public ILayer getLayer() {
		return marker;
	}

}
