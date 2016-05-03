package org.vaadin.addon.leaflet.util;

import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LMarker.DragEndEvent;
import org.vaadin.addon.leaflet.LMarker.DragEndListener;

import com.vividsolutions.jts.geom.Point;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;

public class PointField extends AbstractJTSField<Point> {

    private LMarker marker;

    public PointField() {

    }

    public PointField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    public Class<? extends Point> getType() {
        return Point.class;
    }

    @Override
    protected void prepareEditing() {
        if (marker == null) {
            marker = new LMarker(JTSUtil.toLeafletPoint(getCrsTranslator()
                    .toPresentation(getInternalValue())));	
            map.addLayer(marker);
        }
        
        marker.addDragEndListener(editDragEndListener);        
		map.addClickListener(editClickListener);
        
		map.zoomToContent();
    }

    @Override
    protected void prepareDrawing() {
        map.addClickListener(drawListener);
    }

    @Override
    protected void prepareViewing() {
    	map.removeClickListener(drawListener);
    	map.removeClickListener(editClickListener);
    	if(marker != null) {
    		marker.removeDragEndListener(editDragEndListener);
    	}
    }
    protected LMarker getMarker() {
        return marker;
    }

    private final LeafletClickListener drawListener = new LeafletClickListener() {

        @Override
        public void onClick(LeafletClickEvent event) {
            org.vaadin.addon.leaflet.shared.Point point = event.getPoint();
            setValue(getCrsTranslator().toModel(JTSUtil.toPoint(point)));
            map.removeClickListener(drawListener);
        }
    };
    
    private final LeafletClickListener editClickListener = new LeafletClickListener() {
        @Override
        public void onClick(LeafletClickEvent event) {
            setValue(getCrsTranslator().toModel(
                    JTSUtil.toPoint(event.getPoint())));
            marker.setPoint(event.getPoint());
        }
    };
    
    private final DragEndListener editDragEndListener = new DragEndListener() {
        @Override
        public void dragEnd(DragEndEvent event) {
            setValue(getCrsTranslator()
                    .toModel(JTSUtil.toPoint(marker)));
        }
    };
}
