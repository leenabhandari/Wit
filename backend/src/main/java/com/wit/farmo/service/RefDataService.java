package com.wit.farmo.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class RefDataService {
	
	public List<String> getCropList() {
		return  Arrays.asList("rice",
				"maize",
				"chickpea",
				"kidneybeans",
				"pigeonpeas",
				"mothbeans",
				"mungbean",
				"blackgram",
				"lentil",
				"pomegranate",
				"banana",
				"mango",
				"grapes",
				"watermelon",
				"muskmelon",
				"apple",
				"orange",
				"papaya",
				"coconut",
				"cotton",
				"jute",
				"coffee");
	}
	
	public List<String> getDiseaseList() {
		return Arrays.asList("water-mold",
				"blast",
				"Stem rot",
				"stalk rot",
				"Common Rust",
				"Charcoal-Rot");

	}

}
