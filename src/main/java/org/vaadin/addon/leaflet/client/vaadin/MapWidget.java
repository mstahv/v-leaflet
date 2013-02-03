package org.vaadin.addon.leaflet.client.vaadin;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class MapWidget extends Widget {
	
	public MapWidget() {
		setElement(Document.get().createDivElement());
		setHeight("100%");
		setStyleName("v-leaflet");
	}
	
}
