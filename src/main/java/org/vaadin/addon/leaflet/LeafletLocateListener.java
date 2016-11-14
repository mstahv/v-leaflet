package org.vaadin.addon.leaflet;

import com.vaadin.util.ReflectTools;
import org.vaadin.addon.leaflet.shared.EventId;

import java.lang.reflect.Method;

public interface LeafletLocateListener {
	
	static final Method METHOD = ReflectTools.findMethod(LeafletLocateListener.class, EventId.LOCATE, LeafletLocateEvent.class);
	
	void onLocate(LeafletLocateEvent event);

}