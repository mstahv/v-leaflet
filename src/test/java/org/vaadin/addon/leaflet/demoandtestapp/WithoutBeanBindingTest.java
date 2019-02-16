package org.vaadin.addon.leaflet.demoandtestapp;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.vaadin.addon.leaflet.util.PointField;
import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class WithoutBeanBindingTest extends AbstractTest {
	private WKTReader wkt = new WKTReader();

	@Override
	public String getDescription() {
		return "A simple test for JTSFields without bean binding";
	}

	@Override
	public Component getTestComponent() {
		final PointField pointFieldEmpty = new PointField("empty PointField");
		pointFieldEmpty.setSizeFull();

		final PointField pointFieldInitialized = new PointField("PointField with value");
		pointFieldInitialized.setSizeFull();
		pointFieldInitialized.getMap().setZoomLevel(8);
		pointFieldInitialized.setValue(getPoint());

		Button getValueButton = new Button("Get values");
		getValueButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Point value1 = pointFieldEmpty.getValue();
				Point value2 = pointFieldInitialized.getValue();
				Notification.show(value1 + "\n" + value2);
			}
		});
		HorizontalLayout fieldLayout = new HorizontalLayout(pointFieldEmpty, pointFieldInitialized);
		fieldLayout.setSizeFull();
		fieldLayout.setSpacing(true);
		VerticalLayout layout = new VerticalLayout(fieldLayout, getValueButton);
		layout.setExpandRatio(fieldLayout, 1f);
		layout.setSizeFull();
		layout.setSpacing(true);
		return layout;
	}

	private Geometry readWKT(String wktString) {
		try {
			return wkt.read(wktString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Point getPoint() {
		return (Point) readWKT("POINT (23 64)");
	}
}
