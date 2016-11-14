package org.vaadin.addon.leaflet.shared;

/**
 * Event identifiers for client-server communication. By convention these can be
 * e.g. method names from server side listeners.
 * 
 */
public interface EventId {

	public static final String LOAD = "onLoad";
	public static final String LOADING = "onLoading";
	public static final String MOUSEOVER = "onMouseOver";
	public static final String MOUSEOUT = "onMouseOut";
	public static final String CONTEXTMENU = "onContextMenu";
	public static final String LOCATE = "onLocate";

}
