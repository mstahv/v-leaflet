package org.vaadin.addon.leaflet;

import com.vaadin.util.ReflectTools;

import java.lang.reflect.Method;

public interface LeafletOverlayAddListener {

    static final Method METHOD = ReflectTools.findMethod(LeafletOverlayAddListener.class, "onOverlayAdd", LeafletOverlayAddEvent.class);

    void onOverlayAdd(LeafletOverlayAddEvent event);
}
