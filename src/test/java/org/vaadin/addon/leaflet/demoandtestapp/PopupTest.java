package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.LinkedList;
import java.util.List;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LPopup;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class PopupTest extends AbstractTest implements DetachListener {

	private LMarker lMarker;

	@Override
	public String getDescription() {
		return "Popup test case";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
	    

		leafletMap = new LMap();
        
//        leafletMap.addLayer(new LOpenStreetMapLayer());

		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);
        
        
        lMarker = new LMarker(60.4525, 22.301);
        lMarker.setPopup("Popup tied to marker");
		leafletMap.addComponent(lMarker);
        lMarker.openPopup();
        
        
        LPolyline lPolyline = new LPolyline(new Point(60.4525, 22.301),new Point(60.4539, 22.309));
        lPolyline.setPopup("Popup tied to polyline");
        leafletMap.addComponent(lPolyline);
        lPolyline.openPopup();
        
        LPopup lPopup = new LPopup(60.4525, 22.301).setContent("Hi, can't close me!");
        leafletMap.addComponent(lPopup);
        lPopup.addDetachListener(this);
        popups.add(lPopup);
        
	    
		return leafletMap;

	}
	
	List<LPopup> popups = new LinkedList<LPopup>();

	@Override
	protected void setup() {
		super.setup();
		
        final TextField lat = new TextField("Lat");
        lat.setValue(""+60.4525);
        content.addComponentAsFirst(lat);
        
        final TextField lon = new TextField("Lon");
        lon.setValue(""+22.301);
        content.addComponentAsFirst(lon);
        
        final TextField html = new TextField("html");
        content.addComponentAsFirst(html);
        
        final CheckBox closeButton = new CheckBox("Close button");
        content.addComponentAsFirst(closeButton);

        final CheckBox closeonclick = new CheckBox("Close on click");
        content.addComponentAsFirst(closeonclick);

        content.addComponentAsFirst(new Button("Open popup", new ClickListener(){

            @Override
            public void buttonClick(Button.ClickEvent event) {
            	Double lonValue = Double.valueOf(lon.getValue());
            	Double latValue = Double.valueOf(lat.getValue());
                LPopup lPopup = new LPopup(latValue, lonValue).setContent(html.getValue());
                lPopup.setCloseButton(closeButton.getValue());
                lPopup.setCloseOnClick(closeonclick.getValue());
                leafletMap.addComponent(lPopup);
                leafletMap.zoomToContent();
                
                popups.add(lPopup);
                
                lPopup.addDetachListener(PopupTest.this);
            }
        }));

        content.addComponentAsFirst(new Button("Remove first", new ClickListener(){

            @Override
            public void buttonClick(Button.ClickEvent event) {
            	popups.remove(0).close();
            }
        }));

	}

	@Override
	public void detach(DetachEvent event) {
		LPopup popup = (LPopup) event.getConnector();
		Notification.show("Detached " +popup.getContent());
		
	}
}
