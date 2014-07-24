package org.vaadin.addon.leaflet;

import com.vaadin.event.*;
import com.vaadin.server.*;

public class LeafletLoadEvent extends ConnectorEvent
{
   public LeafletLoadEvent(ClientConnector source)
   {
      super(source);
   }
}