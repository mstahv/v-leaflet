package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;


import com.vaadin.util.ReflectTools;

public interface LeafletMouseOutListener {
	
	static final Method METHOD = ReflectTools.findMethod(LeafletMouseOutListener.class, "onMouseOut", LeafletMouseOutEvent.class);
	
	void onMouseOut(LeafletMouseOutEvent event);
}