package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.ClientConnector.DetachEvent;
import java.util.LinkedList;
import java.util.List;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LPopup;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.shared.PopupState;
import org.vaadin.addonhelpers.AbstractTest;

@StyleSheet({"testStyles.css"})
public class PopupTest extends AbstractTest implements DetachListener {

	private LMarker lMarker;
	private LMarker lMarker2;

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

        lMarker2 = new LMarker(60.4533, 22.309);
        lMarker2.setPopup("Popup tied to marker with...<br/> max height 20");
        PopupState popupState1 = new PopupState();
        popupState1.maxHeight = 20;
        lMarker2.setPopupState(popupState1);
        leafletMap.addComponent(lMarker2);
        lMarker2.openPopup();

        LPolyline lPolyline = new LPolyline(new Point(60.4525, 22.301),new Point(60.4539, 22.309));
        lPolyline.setPopup("Popup tied to polyline");
        leafletMap.addComponent(lPolyline);
        lPolyline.openPopup();
        
        LPopup lPopup = new LPopup(60.4525, 22.301).setContent("Hi, can't close me!");
        lPopup.setStyleName("jorma");
        leafletMap.addComponent(lPopup);
        lPopup.addDetachListener(this);

        LPopup lPopup2 = new LPopup(60.4540, 22.291).setContent("Hi, my max height ..<br/> is 30");
        PopupState popupState2 = new PopupState();
        popupState2.maxHeight = 30;
        lPopup2.setPopupState(popupState2);
        leafletMap.addComponent(lPopup2);

        LPopup lPopup3 = new LPopup(60.4560, 22.310).setContent("Hi, i've got a custom style in magenta!");
        lPopup3.setStyleName("popup-custom-style-test");
        leafletMap.addComponent(lPopup3);

        lPopup.addClickListener(new LeafletClickListener() {
            @Override
            public void onClick(LeafletClickEvent event) {
                Notification.show("Jorma was clicked at a Popup anchored to " + event.getPoint() + " XY:" + event.getClientX() + " " + event.getClientY());
            }
        });
        
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
