package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;


import com.vaadin.util.ReflectTools;

public interface LeafletClickListener {
	
	static final Method METHOD = ReflectTools.findMethod(LeafletClickListener.class, "onClick", LeafletClickEvent.class);
	
	void onClick(LeafletClickEvent event);
}