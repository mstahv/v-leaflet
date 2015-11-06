package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

public interface ContextMenuServerRpc extends ServerRpc {

    void onContextMenu(Point p, MouseEventDetails d);

}
