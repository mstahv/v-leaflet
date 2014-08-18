package org.vaadin.addon.leaflet.junit;

import org.junit.Test;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Point;

public class OnePointLineAndZoomToContent {
	
	@Test
	public void test() {
		LMap lMap = new LMap();
		lMap.addComponent(new LPolyline(new Point(0,0)));
		lMap.zoomToContent();
	}

}
