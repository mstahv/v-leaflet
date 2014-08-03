package org.vaadin.addon.leaflet;

import java.lang.reflect.*;

import org.vaadin.addon.leaflet.shared.EventId;

import com.vaadin.util.*;

public interface LeafletLoadListener
{
   static final Method METHOD = ReflectTools.findMethod(
	 LeafletLoadListener.class, EventId.LOAD, LeafletLoadEvent.class);

   void onLoad(LeafletLoadEvent event);
}