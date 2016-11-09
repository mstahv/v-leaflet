package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.control.LayersOptions;
import org.vaadin.addon.leaflet.shared.LeafletLayersState;
import org.peimari.gleaflet.client.control.Layers;
import org.vaadin.addon.leaflet.control.LLayers;
import org.vaadin.addon.leaflet.shared.LayerControlInfo;

import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;
import java.util.HashMap;
import java.util.HashSet;
import org.peimari.gleaflet.client.Layer;

@Connect(LLayers.class)
public class LeafletLayersConnector extends AbstractControlConnector<Layers> {
    
    HashMap<LayerControlInfo, Layer> infoToLayer = new HashMap<>();

	@Override
	protected Layers createControl() {
        LayersOptions o = createOptions();
		Layers l = Layers.create(o);
		getParent().setLayersControl(l);
		return l;
	}

    protected LayersOptions createOptions() {
        LayersOptions o = LayersOptions.create();
        LeafletLayersState s = getState();

        if (s.collapsed != null) {
            o.setCollapsed(s.collapsed);
        }
        return o;
    }

	protected void doStateChange(StateChangeEvent stateChangeEvent) {
        if(!infoToLayer.isEmpty()) {
            // redrawing list, remove all and then add all
            // TODO optimize if somebody uses hundreds of layers
            for (LayerControlInfo layerControlInfo : infoToLayer.keySet()) {
                getControl().removeLayer(infoToLayer.get(layerControlInfo));
            }
            infoToLayer.clear();
        }
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
                infoToLayer.put(layerControlInfo, layerConnector.getLayer());
			}
		}
	}

	@Override
	public LeafletLayersState getState() {
		return (LeafletLayersState) super.getState();
	}

}
