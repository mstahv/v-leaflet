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
            marker.addDragEndListener(new DragEndListener() {

                @Override
                public void dragEnd(DragEndEvent event) {
                    setValue(getCrsTranslator()
                            .toModel(JTSUtil.toPoint(marker)));
                }
            });
            map.addClickListener(new LeafletClickListener() {

                @Override
                public void onClick(LeafletClickEvent event) {
                    setValue(getCrsTranslator().toModel(
                            JTSUtil.toPoint(event.getPoint())));
                    marker.setPoint(event.getPoint());
                }
            });

            map.addLayer(marker);
        }
    }

    @Override
    protected void prepareDrawing() {
        map.addClickListener(listener);
    }

    protected LMarker getMarker() {
        return marker;
    }

    private final LeafletClickListener listener = new LeafletClickListener() {

        @Override
        public void onClick(LeafletClickEvent event) {
            org.vaadin.addon.leaflet.shared.Point point = event.getPoint();
            setValue(getCrsTranslator().toModel(JTSUtil.toPoint(point)));
            map.removeClickListener(listener);
        }
    };

}
