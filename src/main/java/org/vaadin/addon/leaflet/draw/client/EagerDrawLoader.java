package org.vaadin.addon.leaflet.draw.client;

import org.peimari.gleaflet.client.resources.LeafletDrawResourceInjector;

import com.google.gwt.core.client.EntryPoint;

public class EagerDrawLoader implements EntryPoint {

	@Override
	public void onModuleLoad() {
		LeafletDrawResourceInjector.ensureInjected();
	}

}
