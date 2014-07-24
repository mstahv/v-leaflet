package org.vaadin.addon.leaflet;

import java.lang.reflect.*;

import com.vaadin.util.*;

public interface LeafletLoadingListener
{
   static final Method METHOD = ReflectTools.findMethod(
	 LeafletLoadingListener.class, "onLoading", LeafletLoadingEvent.class);

   void onLoading(LeafletLoadingEvent event);
}