package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;


import com.vaadin.util.ReflectTools;

public interface LeafletMouseOverListener {
	
	static final Method METHOD = ReflectTools.findMethod(LeafletMouseOverListener.class, "onMouseOver", LeafletMouseOverEvent.class);
	
	void onMouseOver(LeafletMouseOverEvent event);
}