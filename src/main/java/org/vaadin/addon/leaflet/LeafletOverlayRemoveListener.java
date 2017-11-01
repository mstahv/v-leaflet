package org.vaadin.addon.leaflet;

import com.vaadin.util.ReflectTools;

import java.lang.reflect.Method;

public interface LeafletOverlayRemoveListener {

    static final Method METHOD = ReflectTools.findMethod(LeafletOverlayRemoveListener.class, "onOverlayRemove", LeafletOverlayRemoveEvent.class);

    void onOverlayRemove(LeafletOverlayRemoveEvent event);
}
