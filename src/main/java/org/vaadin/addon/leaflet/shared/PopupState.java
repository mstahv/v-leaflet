package org.vaadin.addon.leaflet.shared;

import java.io.Serializable;

public class PopupState implements Serializable {
	public int maxWidth = 300;
	public int minWidth = 50;
	public int maxHeight = 0;
	public boolean autoPan;
	public boolean closeButton = true;
    public boolean closeOnClick = true;
    public boolean autoClose = true;
    public boolean keepInView;
	public Point offset;
	public Point autoPanPadding;
	public boolean zoomAnimation = true;
}
