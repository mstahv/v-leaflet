package org.vaadin.addon.leaflet.jsonmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.Serializable;
import java.util.HashMap;
import static org.vaadin.addon.leaflet.jsonmodels.VectorStyle.mapper;

/**
 * @author mstahv
 */
public class BasicMap extends HashMap<String, Serializable> {

    public String asJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
