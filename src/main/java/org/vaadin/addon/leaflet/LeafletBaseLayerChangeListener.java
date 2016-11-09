package org.vaadin.addon.leaflet;

import com.vaadin.util.ReflectTools;

import java.lang.reflect.Method;

public interface LeafletBaseLayerChangeListener {

    static final Method METHOD = ReflectTools.findMethod(LeafletBaseLayerChangeListener.class, "onBaseLayerChange", LeafletBaseLayerChangeEvent.class);

    void onBaseLayerChange(LeafletBaseLayerChangeEvent event);
}
