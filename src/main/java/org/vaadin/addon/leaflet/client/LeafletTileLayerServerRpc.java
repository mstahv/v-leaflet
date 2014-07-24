package org.vaadin.addon.leaflet.client;

import com.vaadin.shared.annotations.*;
import com.vaadin.shared.communication.*;

public interface LeafletTileLayerServerRpc extends ServerRpc
{
  // @Delayed(lastOnly = true)
   void onLoad();

   //@Delayed(lastOnly = true)
   void onLoading();
}
