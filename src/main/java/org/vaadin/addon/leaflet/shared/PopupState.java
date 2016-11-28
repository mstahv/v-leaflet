package org.vaadin.addon.leaflet.shared;

public class PopupState extends DivOverlayState {
	public int maxWidth = 300;
	public int minWidth = 50;
	public int maxHeight = 0;
	public boolean autoPan;
	public boolean closeButton = true;
    public boolean closeOnClick = true;
    public boolean autoClose = true;
    public boolean keepInView;
	public Point autoPanPadding;
	public boolean zoomAnimation = true;
}
