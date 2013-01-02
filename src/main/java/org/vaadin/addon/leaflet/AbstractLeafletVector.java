package org.vaadin.addon.leaflet;

public abstract class AbstractLeafletVector extends LeafletLayer {

	public AbstractLeafletVector() {
		super();
	}
	
	public void setColor(String color) {
		getState().color = color;
	}

}