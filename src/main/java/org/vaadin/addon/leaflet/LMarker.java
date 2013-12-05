package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;

import org.vaadin.addon.leaflet.client.DragEndServerRpc;
import org.vaadin.addon.leaflet.client.LeafletMarkerClientRpc;
import org.vaadin.addon.leaflet.client.LeafletMarkerState;
import org.vaadin.addon.leaflet.client.PopupState;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.util.ReflectTools;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Prototype. This might be technically easier to implement as an extension with
 * Leaflet, but I'm trying this way to pave way for OL integration (which has
 * vectorlayer, extensions cannot have children -> needs to be
 * componentcontainer)
 * 
 */
public class LMarker extends AbstractLeafletLayer {
	
	public static class DragEndEvent extends Event {

		public DragEndEvent(LMarker source) {
			super(source);
		}
		
	}
	
	public interface DragEndListener {
		Method METHOD = ReflectTools.findMethod(DragEndListener.class, "dragEnd", DragEndEvent.class);

		public void dragEnd(DragEndEvent event);
	}

    @Override
    protected LeafletMarkerState getState() {
        return (LeafletMarkerState) super.getState();
    }

    public LMarker(double lat, double lon) {
    	this();
        getState().point = new Point(lat, lon);
    }

    public LMarker() {
		registerRpc(new DragEndServerRpc() {
			
			@Override
			public void dragEnd(Point point) {
				setPoint(point);
				fireEvent(new DragEndEvent(LMarker.this));
			}
		});

    }

    public LMarker(Point point) {
    	this();
        getState().point = point;
    }
    
    public LMarker(com.vividsolutions.jts.geom.Point jtsPoint) {
    	this(JTSUtil.toLeafletPoint(jtsPoint));
    }

    public void setPoint(Point p) {
        getState().point = p;
    }

    public Point getPoint() {
        return getState().point;
    }

    public void setIconSize(Point point) {
        getState().iconSize = point;
    }

    public void setIconAnchor(Point point) {
        getState().iconAnchor = point;
    }

    public void setTitle(String title) {
        getState().title = title;
    }

    public void setDivIcon(String divIcon) {
        getState().divIcon = divIcon;
    }

    public void setPopup(String popup) {
        getState().popup = popup;
    }

    public void setPopupState(PopupState popupState){
        getState().popupState = popupState;
    }

    public void openPopup() {
        getRpcProxy(LeafletMarkerClientRpc.class).openPopup();
    }

    public void closePopup() {
        getRpcProxy(LeafletMarkerClientRpc.class).closePopup();
    }
    
	public void addDragEndListener(DragEndListener listener) {
		addListener("dragend", DragEndEvent.class, listener,
				DragEndListener.METHOD);
	}
	
	public void removeDragEndListener(DragEndListener listener) {
		removeListener("dragend", DragEndEvent.class, listener);
	}

	@Override
	public Geometry getGeometry() {
		return JTSUtil.toPoint(this);
	}


}
