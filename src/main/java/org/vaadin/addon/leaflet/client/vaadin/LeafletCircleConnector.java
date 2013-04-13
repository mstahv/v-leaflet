package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.vector.Circle;
import org.discotools.gwt.leaflet.client.layers.vector.PathOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LCircle.class)
public class LeafletCircleConnector extends
        AbstractLeafletLayerConnector<PathOptions> {

    private Circle marker;

    @Override
    public LeafletCircleState getState() {
        return (LeafletCircleState) super.getState();
    }

    @Override
    protected PathOptions createOptions() {
        PathOptions pathOptions = new PathOptions();
        pathOptions.setColor(getState().color);
        return pathOptions;
    }

    @Override
    protected void update() {
        if (marker != null) {
            removeLayerFromParent();
            EventHandlerManager.clearEventHandler(marker, Events.click);
        }
        LatLng latlng = new LatLng(getState().point.getLat(),
                getState().point.getLon());
        PathOptions options = createOptions();
        marker = new Circle(latlng, 200, options);
        addToParent(marker);

        EventHandler<?> handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rpc.onClick(new Point(event.getLatLng().lat(), event
                        .getLatLng().lng()));
            }
        };

        EventHandlerManager.addEventHandler(marker, Events.click, handler);

    }

    @Override
    public ILayer getLayer() {
        return marker;
    }

}
