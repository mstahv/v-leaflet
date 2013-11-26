package org.vaadin.addon.leaflet.client.vaadin;

import org.peimari.gleaflet.client.EditableMap;
import org.peimari.gleaflet.client.draw.DrawFeature;
import org.peimari.gleaflet.client.resources.LeafletDrawResourceInjector;

import com.google.gwt.core.client.JavaScriptObject;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;

public abstract class AbstractLeafletDrawFeatureConnector extends
		AbstractExtensionConnector {
	
	static {
		LeafletDrawResourceInjector.ensureInjected();
	}

	private EditableMap map;
	protected DrawFeature drawFeature;
	protected JavaScriptObject listenerRegistration;

	public AbstractLeafletDrawFeatureConnector() {
		super();
	}

	@Override
	protected void extend(final ServerConnector target) {
	}

	public EditableMap getMap() {
		if(map == null) {
			final LeafletMapConnector mapC = (LeafletMapConnector) getParent();
			map = mapC.getMap().cast();
		}
		return map;
	}

	@Override
	public void onUnregister() {
		super.onUnregister();
		drawFeature.disable();
		if (listenerRegistration != null) {
			map.removeListener(listenerRegistration);
		}
	}

}