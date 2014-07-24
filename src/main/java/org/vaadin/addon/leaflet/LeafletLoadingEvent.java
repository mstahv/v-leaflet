package org.vaadin.addon.leaflet;

import com.vaadin.event.*;
import com.vaadin.server.*;

public class LeafletLoadingEvent extends ConnectorEvent
{
   public LeafletLoadingEvent(ClientConnector source)
   {
      super(source);
   }
}