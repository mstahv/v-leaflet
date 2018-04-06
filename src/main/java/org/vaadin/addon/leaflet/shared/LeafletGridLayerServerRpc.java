package org.vaadin.addon.leaflet.shared;

import com.vaadin.shared.communication.ServerRpc;

public interface LeafletGridLayerServerRpc extends ServerRpc {

    // @Delayed(lastOnly = true)
    void onLoad();

    //@Delayed(lastOnly = true)
    void onLoading();

}
