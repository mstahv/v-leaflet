package org.vaadin.addon.leaflet.demoandtestapp.util;

import com.vaadin.annotations.Widgetset;
import org.vaadin.addonhelpers.TServer;

@Widgetset("org.vaadin.addon.leaflet.Widgetset")
public class UiRunner extends TServer {

	/**
     * Starts and embedded server for the tests
	 */
	public static void main(String[] args) throws Exception {
        new UiRunner().startServer();
	}
}
