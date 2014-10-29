package org.vaadin.addon.leaflet.jsonmodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;

public class VectorStyle implements Serializable {
    
    static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private Boolean stroke;

    private Boolean fill;

    private String fillColor;
    
    private Double fillOpacity;

    private Integer weight;

    private Double opacity;

    private String dashArray;

    private String lineCap;

   private String lineJoin;

    private String color;
	
	public void setFill(Boolean fill) {
		this.fill = fill;
	}
	
	public Boolean getFill() {
		return fill;
	}
	
	public void setOpacity(Double opacity) {
		this.opacity = opacity;
	}
	
	public Double getOpacity() {
		return opacity;
	}
	
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}
	
	public String getFillColor() {
		return fillColor;
	}

    public Double getFillOpacity() {
        return fillOpacity;
    }

    public void setFillOpacity(Double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }
	
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	public Integer getWeight() {
		return weight;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}

	public Boolean getStroke() {
		return stroke;
	}

	public void setStroke(Boolean stroke) {
		this.stroke = stroke;
	}

	public String getDashArray() {
		return dashArray;
	}

	public void setDashArray(String dashArray) {
		this.dashArray = dashArray;
	}

	public String getLineCap() {
		return lineCap;
	}

	public void setLineCap(String lineCap) {
		this.lineCap = lineCap;
	}

	public String getLineJoin() {
		return lineJoin;
	}

	public void setLineJoin(String lineJoin) {
		this.lineJoin = lineJoin;
	}
    
    public String asJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
