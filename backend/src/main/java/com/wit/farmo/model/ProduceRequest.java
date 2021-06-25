package com.wit.farmo.model;

import lombok.Getter;
import lombok.Setter;

//unused
@Getter
@Setter
public class ProduceRequest {
	
	private String state;
	private String district;
	private String season;
	private String crop;
	private Integer year; //TODO
	private Long area; 
}
