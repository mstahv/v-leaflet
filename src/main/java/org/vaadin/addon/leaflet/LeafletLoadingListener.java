package org.vaadin.addon.leaflet;

import java.lang.reflect.*;

import org.vaadin.addon.leaflet.shared.EventId;

import com.vaadin.util.*;

public interface LeafletLoadingListener
{
   static final Method METHOD = ReflectTools.findMethod(
	 LeafletLoadingListener.class, EventId.LOADING, LeafletLoadingEvent.class);

   void onLoading(LeafletLoadingEvent event);
}