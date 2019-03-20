package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;

import org.vaadin.addon.leaflet.shared.DragEndServerRpc;
import org.vaadin.addon.leaflet.shared.LeafletMarkerClientRpc;
import org.vaadin.addon.leaflet.shared.LeafletMarkerState;
import org.vaadin.addon.leaflet.shared.TooltipState;
import org.vaadin.addon.leaflet.shared.PopupState;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.util.ReflectTools;
import org.locationtech.jts.geom.Geometry;

/**
 * A Marker displayed on LMap.
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

    public LMarker(final double lat, final double lon) {
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

    public LMarker(final Point point) {
        this();
        getState().point = point;
    }
    

    public LMarker(final org.locationtech.jts.geom.Point jtsPoint) {
        this(JTSUtil.toLeafletPoint(jtsPoint));
    }

    public void setPoint(final Point p) {
        getState().point = p;
    }

    public Point getPoint() {
        return getState().point;
    }

    public void setIconSize(final Point point) {
        getState().iconSize = point;
    }

    public void setIconAnchor(final Point point) {
        getState().iconAnchor = point;
    }

    public void setIconPathFill(final String pathFill) {
        getState().iconPathFill = pathFill;
    }

    public void setIconPathStroke(final String pathStroke) {
        getState().iconPathStroke = pathStroke;
    }

    public void setIconTextFill(final String textFill) {
        getState().iconTextFill = textFill;
    }

    public void setPopupAnchor(final Point point) {
        getState().popupAnchor = point;
    }

    public void setTitle(final String title) {
        getState().title = title;
    }

    public void setTooltip(final String tooltip) {
        getState().tooltip = tooltip;
    }

    public void setTooltipState(final TooltipState tooltipState) {
        getState().tooltipState = tooltipState;
    }

    public void openTooltip() {
        getRpcProxy(LeafletMarkerClientRpc.class).openTooltip();
    }

    public void closeTooltip() {
        getRpcProxy(LeafletMarkerClientRpc.class).closeTooltip();
    }

    public void setDivIcon(final String divIcon) {
        getState().divIcon = divIcon;
    }

    public void setPopup(final String popup) {
        getState().popup = popup;
    }

    public void setPopupState(final PopupState popupState) {
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

    public void setZIndexOffset(Integer zIndexOffset) {
        getState().zIndexOffset = zIndexOffset;
    }


}
