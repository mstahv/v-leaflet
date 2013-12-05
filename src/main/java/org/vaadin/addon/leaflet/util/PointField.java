package org.vaadin.addon.leaflet.util;

import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LMarker.DragEndEvent;
import org.vaadin.addon.leaflet.LMarker.DragEndListener;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnListener;
import org.vaadin.addon.leaflet.draw.LDrawMarker;

import com.vividsolutions.jts.geom.Point;

public class PointField extends JTSField<Point> {

	private LMarker marker;

	@Override
	public Class<? extends Point> getType() {
		return Point.class;
	}

	protected void prapareEditing() {
		if (marker == null) {
			marker = new LMarker(JTSUtil.toLeafletPoint(getInternalValue()));
			marker.addDragEndListener(new DragEndListener() {

				@Override
				public void dragEnd(DragEndEvent event) {
					setValue(JTSUtil.toPoint(marker));
				}
			});
			map.addLayer(marker);
			map.setCenter(marker.getPoint());
		}
	}

	protected void prepareDrawing() {
		LDrawMarker draw = new LDrawMarker(map);
		draw.addFeatureDrawnListener(new FeatureDrawnListener() {

			@Override
			public void featureDrawn(FeatureDrawnEvent event) {
				setValue(JTSUtil.toPoint((LMarker) event.getDrawnFeature()));
			}
		});

	}

}
