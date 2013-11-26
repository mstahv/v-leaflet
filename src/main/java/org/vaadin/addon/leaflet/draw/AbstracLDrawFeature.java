package org.vaadin.addon.leaflet.draw;

import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnListener;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;

/**
 * Drawing extension for Leaflet vectors.
 * <p>
 * Can be used instead of the {@link LDraw} to just draw a specific type. The
 * constructor adds the extension to {@link AbstractLeafletVector}. The
 * extension is automatically removed after first edit event or on removal.
 * 
 */
public abstract class AbstracLDrawFeature extends AbstractExtension implements IHandler {

	public void addFeatureDrawnListener(FeatureDrawnListener listener) {
		addListener(FeatureDrawnEvent.class, listener,
				FeatureDrawnListener.drawnMethod);
	}

	public void removeFeatureDrawnListener(FeatureDrawnListener listener) {
		removeListener(FeatureDrawnEvent.class, listener);
	}

	protected AbstracLDrawFeature(LMap map) {
		addTo(map);
	}

	protected AbstracLDrawFeature() {
	}
	
	public void addTo(LMap map) {
		extend(map);
	}
	
	@Override
	protected void extend(AbstractClientConnector target) {
		super.extend(target);
	}

}
