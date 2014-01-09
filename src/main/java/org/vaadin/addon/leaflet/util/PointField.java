package org.vaadin.addon.leaflet.util;

import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LMarker.DragEndEvent;
import org.vaadin.addon.leaflet.LMarker.DragEndListener;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnListener;
import org.vaadin.addon.leaflet.draw.LDrawMarker;

import com.vividsolutions.jts.geom.Point;

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
			map.addLayer(marker);
			map.setCenter(marker.getPoint());
		}
	}

        @Override
	protected void prepareDrawing() {
		LDrawMarker draw = new LDrawMarker(map);
		draw.addFeatureDrawnListener(new FeatureDrawnListener() {

			@Override
			public void featureDrawn(FeatureDrawnEvent event) {
				setValue(getCrsTranslator().toModel(
						JTSUtil.toPoint((LMarker) event.getDrawnFeature())));
			}
		});

	}

    protected LMarker getMarker() {
        return marker;
    }
    
}
