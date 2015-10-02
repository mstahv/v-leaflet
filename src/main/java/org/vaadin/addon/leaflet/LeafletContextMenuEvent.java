package org.vaadin.addon.leaflet;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.shared.Point;

public class LeafletContextMenuEvent extends LeafletClickEvent {

    LeafletContextMenuEvent(Component c, Point p, MouseEventDetails d) {
        super(c, p, d);
    }

}
