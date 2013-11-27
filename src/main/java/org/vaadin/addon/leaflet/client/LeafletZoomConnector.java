package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.control.Control;
import org.vaadin.addon.leaflet.control.LZoom;

import com.vaadin.shared.ui.Connect;

@Connect(LZoom.class)
public class LeafletZoomConnector<Zoom> extends AbstractDefaultControl {

	@Override
	protected Control createControl() {
		return getMap().getZoomControl();
	}
	
}
