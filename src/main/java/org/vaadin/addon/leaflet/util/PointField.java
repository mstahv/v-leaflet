package org.vaadin.addon.leaflet.util;

import com.vaadin.shared.Registration;
import org.locationtech.jts.geom.Point;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LMarker.DragEndEvent;
import org.vaadin.addon.leaflet.LMarker.DragEndListener;

import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;

public class PointField extends AbstractJTSField<Point> {

    private LMarker marker;
    private Registration drawRegistration;
    private Registration clickRegistration;
    private Registration markerRegisration;

    public PointField() {

    }

    public PointField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    protected void prepareEditing(boolean userOriginatedValueChangeEvent) {
        if (marker == null) {
            marker = new LMarker(JTSUtil.toLeafletPoint(getCrsTranslator()
                    .toPresentation(getValue())));
            map.addLayer(marker);
        } else {
            marker.setPoint(JTSUtil.toLeafletPoint(getCrsTranslator()
                    .toPresentation(getValue())));
        }

        drawRegistration = marker.addDragEndListener(editDragEndListener);
        clickRegistration = map.addClickListener(editClickListener);

        map.zoomToContent();
    }

    @Override
    protected void prepareDrawing() {
        if (marker != null) {
            map.removeComponent(marker);
            marker = null;
        }
        markerRegisration = map.addClickListener(drawListener);
    }

    @Override
    protected void prepareViewing() {
        if (drawRegistration != null) {
            drawRegistration.remove();
            drawRegistration = null;
        }
        if (clickRegistration != null) {
            clickRegistration.remove();
            clickRegistration = null;
        }
        if (markerRegisration != null) {
            markerRegisration.remove();
            markerRegisration = null;
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
            if (clickRegistration != null) {
                clickRegistration.remove();
                clickRegistration = null;
            }
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
