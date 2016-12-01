package org.vaadin.addon.leaflet.shared;

import com.vaadin.shared.communication.ClientRpc;

public interface LeafletMarkerClientRpc extends ClientRpc {

	public void openTooltip();
	public void closeTooltip();
	public void openPopup();
	public void closePopup();
}
