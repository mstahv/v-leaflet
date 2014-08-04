package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;


import com.vaadin.util.ReflectTools;
import org.vaadin.addon.leaflet.shared.EventId;

public interface LeafletMouseOverListener {
	
	static final Method METHOD = ReflectTools.findMethod(LeafletMouseOverListener.class, EventId.MOUSEOVER, LeafletMouseOverEvent.class);
	
	void onMouseOver(LeafletMouseOverEvent event);
}