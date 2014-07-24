package org.vaadin.addon.leaflet;

import java.lang.reflect.*;

import com.vaadin.util.*;

public interface LeafletLoadListener
{
   static final Method METHOD = ReflectTools.findMethod(
	 LeafletLoadListener.class, "onLoad", LeafletLoadEvent.class);

   void onLoad(LeafletLoadEvent event);
}