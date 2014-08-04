package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;


import com.vaadin.util.ReflectTools;
import org.vaadin.addon.leaflet.shared.EventId;

public interface LeafletMouseOutListener {
	
	static final Method METHOD = ReflectTools.findMethod(LeafletMouseOutListener.class, EventId.MOUSEOUT, LeafletMouseOutEvent.class);
	
	void onMouseOut(LeafletMouseOutEvent event);
}