package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LMarker.DragEndEvent;
import org.vaadin.addon.leaflet.LMarker.DragEndListener;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedListener;
import org.vaadin.addon.leaflet.draw.LEditing;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.AbstractJTSField;
import org.vaadin.addon.leaflet.util.JTSUtil;
import org.vaadin.addon.leaflet.util.LinearRingField;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vividsolutions.jts.geom.LinearRing;
import org.vaadin.addonhelpers.AbstractTest;

public class EditingTest extends AbstractTest implements
		FeatureModifiedListener {

	@Override
	public String getDescription() {
		return "Test leaflet draws editing feature";
	}

	private LMap leafletMap;
	private LEditing lEditing;

	@Override
	public Component getTestComponent() {
		leafletMap = new LMap();
		leafletMap.setHeight("300px");
		leafletMap.setCenter(0, 0);
		leafletMap.setZoomLevel(0);
		leafletMap.addLayer(new LTileLayer(
				"http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"));

		final LMarker marker = new LMarker(10, 10);
		// adding drag end listener makes marker draggable
		marker.addDragEndListener(new DragEndListener() {
			
			@Override
			public void dragEnd(DragEndEvent event) {
				Notification.show("Marker dragged" + marker.getPoint());
			}
		});

		LCircle circle = new LCircle(new Point(-50, 0), 3000 * 1000);
		LPolyline polyline = new LPolyline(new Point(40, 0), new Point(70, 0));

		LPolygon polygon = new LPolygon(new Point(0, 0), new Point(30, 30),
				new Point(30, 0));
		lEditing = new LEditing(polygon);
		lEditing.addFeatureModifiedListener(this);

		leafletMap.addComponents(marker, polyline, circle, polygon);

		return leafletMap;
	}

	@Override
	protected void setup() {
		super.setup();

		for (final Component c : leafletMap) {
			if (c instanceof AbstractLeafletVector) {
				Button button = new Button("Edit"
						+ c.getClass().getSimpleName());
				button.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if (lEditing != null && lEditing.getParent() != null) {
							lEditing.remove();
						}
						lEditing = new LEditing((AbstractLeafletVector) c);
					}
				});
				content.addComponent(button);
			}
		}
		
		AbstractJTSField<LinearRing> c = new LinearRingField("Linear ring");
		LPolygon polygon = new LPolygon(new Point(0, 0), new Point(30, 30),
				new Point(30, 0));
		LinearRing ring = JTSUtil.toLinearRing(polygon);
		c.setValue(ring);
		c.setHeight("200px");
		c.setWidth("200px");

        content.addComponent(c);
		
		

		content.addComponent(new Button("Stop editing",
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						if (lEditing.getParent() != null) {
							lEditing.remove();
						}
					}
				}));
	}

	@Override
	public void featureModified(FeatureModifiedEvent event) {
		Notification.show("Edited"
				+ event.getModifiedFeature().getClass().getSimpleName());
	}
}
