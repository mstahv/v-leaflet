package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.VectorStyle;


public class AbstractLeafletVectorState extends AbstractLeafletComponentState {
	
	private VectorStyle vectorStyle;
    public Boolean clickable;
    public String pointerEvents;
    public String className;
	public String popup;
	public PopupState popupState;


	public VectorStyle getVectorStyle() {
		if(vectorStyle == null) {
			vectorStyle = new VectorStyle();
		}
		return vectorStyle;
	}

	public void setVectorStyle(VectorStyle style) {
		this.vectorStyle = style;
	}
}