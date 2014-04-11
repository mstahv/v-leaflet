package org.vaadin.addon.leaflet.client;

import java.io.Serializable;
import org.vaadin.addon.leaflet.shared.Point;

public class PopupState implements Serializable {
	public int maxWidth;
	public int minWidth;
	public int maxHeight;
	public boolean autoPan;
	public boolean closeButton;
	public Point offset;
	public Point autoPanPadding;
	public boolean zoomAnimation;
}
