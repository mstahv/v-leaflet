package org.vaadin.addon.leaflet.client;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

public class MapWidget extends Widget {
	
	public MapWidget() {
		setElement(Document.get().createDivElement());
		getElement().getStyle().setPosition(Position.RELATIVE);
		DivElement d = Document.get().createDivElement();
		getElement().appendChild(d);
		d.getStyle().setHeight(100, Unit.PCT);
		setStyleName("v-leaflet");
	}
	
}
