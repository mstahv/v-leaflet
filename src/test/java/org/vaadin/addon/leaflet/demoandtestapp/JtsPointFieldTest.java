package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.Arrays;
import java.util.Date;

import com.vaadin.data.BeanBinder;
import com.vaadin.data.ValidationException;
import org.vaadin.addon.leaflet.util.PointField;
import org.vaadin.addonhelpers.AbstractTest;

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
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.vaadin.addon.leaflet.util.JTSUtil;

public class JtsPointFieldTest extends AbstractTest {

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
        return "A test for JTS Field implementations";
    }

    private JtsPojo pojo = new JtsPojo();
    private Label display = new Label();

    private TextField name = new TextField("Name");
    private DateField date = new DateField("Date");
    private PointField point = new PointField("Point");

    // private Polygon polygon;
    @Override
    public Component getTestComponent() {
        content.setMargin(true);

        display.setContentMode(ContentMode.PREFORMATTED);
        display.setCaption("Pojo state:");
        display.setValue(pojo.toString());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();

        VerticalLayout editorform = new VerticalLayout();
        editorform.setSizeFull();

        editorform.setSpacing(true);
        editorform.setCaption("Edit JTS pojo:");

        TabSheet jtsFields = new TabSheet(point);
        jtsFields.setCaption("JTS fiels:");
        jtsFields.setSizeFull();
        editorform.addComponents(new HorizontalLayout(name, date), jtsFields
        // ,polygon
        );
        editorform.setExpandRatio(jtsFields, 1);

        // TODO switch to helper in Vaadin when available http://dev.vaadin.com/ticket/13068
        final BeanBinder<JtsPojo> beanBinder = new BeanBinder<>(
                JtsPojo.class);
        beanBinder.readBean(pojo);
        beanBinder.bindInstanceFields(this);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(new Button("Save", new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    beanBinder.writeBean(pojo);
                    display.setValue(pojo.toString());
                } catch (ValidationException e) {
                    System.err.println("Validation errors:" +Arrays.toString( e.getBeanValidationErrors().toArray()));
                }
            }
        }));

        buttonLayout.addComponent(new Button("Toggle read only", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                beanBinder.setReadOnly(!beanFieldGroup.isReadOnly());
            }
        }));

        buttonLayout.addComponent(new Button("Assign new empty bean", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                pojo = new JtsPojo();
                beanBinder.readBean(pojo);
                display.setValue(pojo.toString());
            }
        }));

        buttonLayout.addComponent(new Button("Assign bean with preset data", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                pojo = new JtsPojo();
                pojo.setPoint(JTSUtil.toPoint(new org.vaadin.addon.leaflet.shared.Point(61, 22)));
                beanBinder.readBean(pojo);
                display.setValue(pojo.toString());
            }
        }));

        editorform.addComponent(buttonLayout);

        horizontalLayout.addComponents(editorform, display);
        horizontalLayout.setExpandRatio(editorform, 1);
        horizontalLayout.setExpandRatio(display, 1);

        return horizontalLayout;
    }

}
