package org.vaadin.addon.leaflet.client.vaadin;

import org.peimari.gleaflet.client.control.Layers;
import org.vaadin.addon.leaflet.control.LLayers;
import org.vaadin.addon.leaflet.shared.LayerControlInfo;

import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(LLayers.class)
public class LeafletLayersConnector extends AbstractControlConnector<Layers> {

	@Override
	protected Layers createControl() {
		Layers l = Layers.create();
		getParent().setLayersControl(l);
		return l;
	}

	protected void doStateChange(StateChangeEvent stateChangeEvent) {
		for (ServerConnector connector : getParent().getChildren()) {
			if (!(connector instanceof AbstractLeafletLayerConnector<?>)) {
				continue;
			}
			AbstractLeafletLayerConnector<?> layerConnector = (AbstractLeafletLayerConnector<?>) connector;
			LayerControlInfo layerControlInfo = getState().layerContolInfo
					.get(layerConnector);

			if (layerControlInfo != null && layerControlInfo.name != null) {
				if (layerControlInfo.baseLayer) {
					getControl().addBaseLayer(layerConnector.getLayer(),
							layerControlInfo.name);
				} else {
					getControl().addOverlay(layerConnector.getLayer(),
							layerControlInfo.name);
				}
			}
		}
	}

	@Override
	public LeafletLayersState getState() {
		return (LeafletLayersState) super.getState();
	}

}
