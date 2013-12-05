package org.vaadin.addon.leaflet.util;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;

import com.vaadin.client.ui.Field;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * Abstract base class for re-usable Vaadin {@link Field}s that edit JTS types
 * visually with Leaflet based map.
 * 
 * TODO move these to v-leaflet
 * 
 * @param <T>
 */
public abstract class JTSField<T> extends CustomField<T> {

	protected LMap map = new LMap();

	public JTSField() {
		super();
		setValidationVisible(false);
		setSizeFull();
	}

	@Override
	protected Component initContent() {
		map.setZoomLevel(2);
		initBaseLayers();
		return map;
	}

	protected void initBaseLayers() {
		map.addLayer(new LTileLayer(
				"http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"));
	}

	@Override
	protected void setInternalValue(T newValue) {
		super.setInternalValue(newValue);
		if (newValue == null) {
			prepareDrawing();
		} else {
			prapareEditing();
		}
	}

	protected abstract void prapareEditing();

	protected abstract void prepareDrawing();

}