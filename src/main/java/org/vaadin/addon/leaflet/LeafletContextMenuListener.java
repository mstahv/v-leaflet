package org.vaadin.addon.leaflet;

import com.vaadin.util.ReflectTools;
import java.lang.reflect.Method;

public interface LeafletContextMenuListener {

    static final Method METHOD = ReflectTools.findMethod(LeafletContextMenuListener.class, "onContextMenu", LeafletContextMenuEvent.class);

    void onContextMenu(LeafletContextMenuEvent event);
}
