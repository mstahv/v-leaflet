package org.vaadin.addon.leaflet.util;

import org.locationtech.jts.geom.Geometry;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;

import com.vaadin.client.ui.Field;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * Abstract base class for re-usable Vaadin {@link Field}s that edit JTS types
 * visually with Leaflet based map.
 * 
 * <p>
 * By default these fields use OSM tiles as background layer. Note, that their
 * usage policy might not fit to your needs. To configure map (without extending
 * it) you can use {@link #setConfigurator(Configurator)} for each field, or
 * modify the global configuration strategy with
 * {@link #setDefaultConfigurator(Configurator)}
 * 
 * @param <T> the JTS Geometry type of the field
 */
public abstract class AbstractJTSField<T extends Geometry> extends
		CustomField<T> {

	public interface Configurator {

		void configure(AbstractJTSField<?> field);

	}
	private T value;
	private static Configurator defaultConfigurator = new Configurator() {

		@Override
		public void configure(AbstractJTSField<?> field) {
			LTileLayer layer = new LTileLayer(
					"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
			layer.setAttributionString("© OpenStreetMap contributors");
			field.getMap().addLayer(layer);
            // hardcoded support for Leaflet.Editable
            field.getMap().setCustomInitOption("editable", true);
		}
	};

	/**
	 * Sets the default strategy to configure {@link AbstractJTSField}s.
	 * 
	 * @param configurator the strategy to configure JTS fields
	 */
	public static void setDefaultConfigurator(Configurator configurator) {
		if (configurator == null) {
			throw new IllegalArgumentException();
		}
		defaultConfigurator = configurator;
	}

	private static CRSTranslator<Geometry> defaultCRSTranslator = new CRSTranslator<Geometry>() {

		@Override
		public Geometry toPresentation(Geometry geom) {
			assert geom.getSRID() == 0 || geom.getSRID() == 4326;
			return geom;
		}

		@Override
		public Geometry toModel(Geometry geom) {
			assert geom.getSRID() == 0 || geom.getSRID() == 4326;
			return geom;
		}
	};

	/**
	 * Sets the default CRFTranslator to convert values to and from presentation
	 * in WSG86 (EPSG:4326).
	 * 
     * @param translator the translator to be used to convert values to and from
     * presentation
	 */
	@SuppressWarnings("unchecked")
	public static void setDefaultCRFTranslator(
			CRSTranslator<? extends Geometry> translator) {
		if (translator == null) {
			throw new IllegalArgumentException();
		}
		defaultCRSTranslator = (CRSTranslator<Geometry>) translator;
	}

	protected LMap map = new LMap();

	private Configurator configurator;

	private CRSTranslator<T> crsTranslator;

	public AbstractJTSField() {
		super();
		setRequiredIndicatorVisible(false);
		setSizeFull();
	}

	public LMap getMap() {
		return map;
	}

	@Override
	protected Component initContent() {	   
	   	if(map.getZoomLevel() == null ) {
	   	   map.setZoomLevel(2);
	   	}
		initBaseLayers();
		return map;
	}

	/**
	 * Configures base layers and other "extra features" for the field. If no
	 * custom configurator is set using {@link #setConfigurator(Configurator)},
	 * a static defaultConfigurator is used.
	 */
	protected void initBaseLayers() {
		if (getConfigurator() == null) {
			defaultConfigurator.configure(this);
		} else {
			getConfigurator().configure(this);
		}
	}
    
    private boolean userOriginatedSetValueEvent;

    @Override
    protected boolean setValue(T value, boolean userOriginated) {
        userOriginatedSetValueEvent = userOriginated;
        return super.setValue(value, userOriginated); //To change body of generated methods, choose Tools | Templates.
    }

	@Override
	protected void doSetValue(T newValue) {
		this.value = newValue;
		if (newValue == null) {
			prepareDrawing();
		} else {
			prepareEditing(userOriginatedSetValueEvent);
		}
	}

	@Override
	public T getValue() {
		return value;
	}

	protected abstract void prepareEditing(boolean userOriginatedValueChange);

	protected abstract void prepareDrawing();

	protected abstract void prepareViewing();
	
	public Configurator getConfigurator() {
		return configurator;
	}

	public void setConfigurator(Configurator configurator) {
		this.configurator = configurator;
	}

	@SuppressWarnings("unchecked")
	public CRSTranslator<T> getCrsTranslator() {
		if (crsTranslator == null) {
			return (CRSTranslator<T>) defaultCRSTranslator;
		}
		return crsTranslator;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setCRSTranslator(CRSTranslator crsTranslator) {
		this.crsTranslator = crsTranslator;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(readOnly == true) {
			prepareViewing();
		} else {
			if(getValue() == null) {
				prepareDrawing();
			} else {
				prepareEditing(false);
			}
		}
	}

    @Override
    public void attach() {
		super.attach();
		if (getValue() == null) {
			prepareDrawing();
		}
	}

}