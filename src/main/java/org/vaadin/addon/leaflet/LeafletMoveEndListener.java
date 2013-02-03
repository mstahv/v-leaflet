package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;


import com.vaadin.util.ReflectTools;

public interface LeafletMoveEndListener {
	
	static final Method METHOD = ReflectTools.findMethod(LeafletMoveEndListener.class, "onMoveEnd", LeafletMoveEndEvent.class);
	
	void onMoveEnd(LeafletMoveEndEvent event);
}