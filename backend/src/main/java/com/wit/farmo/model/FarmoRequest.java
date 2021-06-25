package com.wit.farmo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FarmoRequest {
	private Double lat;
	private Double lon;
	private Double area;
	private Soil soil;
}
