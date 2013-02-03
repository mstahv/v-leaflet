package org.vaadin.addon.leaflet.client.vaadin;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

public class MapWidget extends Widget {
	
	static private int count = 0;
	private String id = "leafletmap" + count++;

	DivElement container = Document.get().createDivElement();
	
	public MapWidget() {
		setElement(Document.get().createDivElement());
		getElement().appendChild(container);
		container.setId(id);
		Style style = container.getStyle();
		style.setHeight(100, Unit.PCT);
		style.setWidth(100, Unit.PCT);
		style.setPosition(Position.RELATIVE);
		setHeight("100%");
	}
	
	public String getId() {
		return id;
	}

}
