package org.vaadin.addon.leaflet.shared;


public class LeafletPopupState extends AbstractLeafletComponentState {

    {
        primaryStyleName = "l-popup";
    }
    
	public Point point;
	public String htmlContent;
    public PopupState popupState = new PopupState();

}
