package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.Date;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.util.CRSTranslator;
import org.vaadin.addon.leaflet.util.LineStringField;
import org.vaadin.addon.leaflet.util.LinearRingField;
import org.vaadin.addon.leaflet.util.PointField;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

public class JtsFieldWithProjectionTest extends AbstractTest {

	private static final int EPSG_3067 = 3067;

	public static class JtsPojo {
		private String name;
		private Date date;
		private Point point;
		private LineString lineString;
		private LinearRing linearRing;
		private Polygon polygon;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

		public LineString getLineString() {
			return lineString;
		}

		public void setLineString(LineString lineString) {
			this.lineString = lineString;
		}

		public Polygon getPolygon() {
			return polygon;
		}

		public void setPolygon(Polygon polygon) {
			this.polygon = polygon;
		}

		@Override
		public String toString() {
			return "JtsPojo [\n name=" + name + ",\n date=" + date
					+ ",\n point=" + point + ",\n lineString=" + lineString
					+ ",\n linearRing=" + linearRing + ",\n polygon=" + polygon
					+ "\n]";
		}

		public LinearRing getLinearRing() {
			return linearRing;
		}

		public void setLinearRing(LinearRing linearRing) {
			this.linearRing = linearRing;
		}
	}

	@Override
	public String getDescription() {
		return "A test for JTS Field implementations with custom projection";
	}

	private JtsPojo pojo = new JtsPojo();
	private Label display = new Label();

	private TextField name = new TextField("Name");
	private DateField date = new DateField("Date");
	private PointField point = new PointField("Point");
	private LineStringField lineString = new LineStringField("LineStringField");
	private LinearRingField linearRing = new LinearRingField("LinearRingField");

	@Override
	public Component getTestComponent() {
		content.setMargin(true);

		try {
			
			/*
			 * Build geotools based coordinate reference system translator
			 * ETRS-TM35FIN (model) <> WGS84 (presentation)
			 */
			
			CoordinateReferenceSystem tm35fin = CRS.decode("EPSG:" + EPSG_3067);
			// GIS weirdness, x/y whatever, always...
			boolean longitudeFirst = true;
			CRSAuthorityFactory factory = CRS
					.getAuthorityFactory(longitudeFirst);
			CoordinateReferenceSystem wsg86 = factory
					.createCoordinateReferenceSystem("EPSG:4326");
			boolean lenient = true; // allow for some error due to different
									// datums
			final MathTransform toWsg86 = CRS.findMathTransform(tm35fin, wsg86,
					lenient);
			final MathTransform toModel = toWsg86.inverse();
			
			// Build the actual v-leaflet JTS field translator
			CRSTranslator<Geometry> translator = new CRSTranslator<Geometry>() {

				@Override
				public Geometry toPresentation(Geometry geom) {
					try {
						return JTS.transform(geom, toWsg86);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				@Override
				public Geometry toModel(Geometry geom) {
					try {
						return JTS.transform(geom, toModel);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
			point.setCRSTranslator(translator);
			linearRing.setCRSTranslator(translator);
			lineString.setCRSTranslator(translator);
			// Also can be set web app global CRFTranslator
			// AbstractJTSField.setDefaultCRFTranslator(translator);
		} catch (NoSuchAuthorityCodeException e) {
			throw new RuntimeException(e);
		} catch (FactoryException e) {
			throw new RuntimeException(e);
		} catch (MismatchedDimensionException e) {
			throw new RuntimeException(e);
		} catch (TransformException e) {
			throw new RuntimeException(e);
		}

		// Vaadin office at ETRS-TM35FIN (EPSG:3067)
		GeometryFactory geometryFactory = new GeometryFactory(
				new PrecisionModel(), EPSG_3067);
		Point p = geometryFactory.createPoint(new Coordinate(241637, 6711028));
		pojo.setPoint(p);

		display.setContentMode(ContentMode.PREFORMATTED);
		display.setCaption("Pojo state:");
		display.setValue(pojo.toString());

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();

		VerticalLayout editorform = new VerticalLayout();
		editorform.setSizeFull();

		editorform.setSpacing(true);
		editorform.setCaption("Edit JTS pojo:");

		TabSheet jtsFields = new TabSheet(point, lineString, linearRing);
		jtsFields.setCaption("JTS fiels:");
		jtsFields.setSizeFull();
		editorform.addComponents(new HorizontalLayout(name, date), jtsFields
		// ,polygon
				);
		editorform.setExpandRatio(jtsFields, 1);

		// TODO switch to helper in Vaadin when available
		// http://dev.vaadin.com/ticket/13068
		final BeanFieldGroup<JtsPojo> beanFieldGroup = new BeanFieldGroup<JtsPojo>(
				JtsPojo.class);
		beanFieldGroup.setItemDataSource(pojo);
		beanFieldGroup.bindMemberFields(this);

		Button c = new Button("Save", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					beanFieldGroup.commit();
					display.setValue(pojo.toString());
				} catch (CommitException e) {
					e.printStackTrace();
				}
			}
		});
		c.setImmediate(true);
		c.setId("SSS");
		editorform.addComponent(c);

		horizontalLayout.addComponents(editorform, display);
		horizontalLayout.setExpandRatio(editorform, 1);
		horizontalLayout.setExpandRatio(display, 1);

		return horizontalLayout;
	}

}
