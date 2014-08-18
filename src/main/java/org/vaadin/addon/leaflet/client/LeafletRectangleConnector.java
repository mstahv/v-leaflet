package org.vaadin.addon.leaflet.client;

import com.vaadin.shared.ui.Connect;

/**
 * Special connector for Rectangle only needed for drawing.
 *
 */
@Connect(org.vaadin.addon.leaflet.LRectangle.class)
public class LeafletRectangleConnector extends LeafletPolygonConnector {
}
