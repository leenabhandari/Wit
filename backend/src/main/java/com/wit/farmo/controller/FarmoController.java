package com.wit.farmo.controller;

import com.wit.farmo.model.FarmoRequest;
import com.wit.farmo.model.FinalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wit.farmo.service.FarmoService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin
@RequestMapping
public class FarmoController {

	@Autowired
	FarmoService farmoService;
	
	@PostMapping
	public ResponseEntity<FinalResponse> getSystemPlan(@RequestBody FarmoRequest request) {
		return new ResponseEntity<>(farmoService.getRecommendation(request),HttpStatus.OK);
	}
	
//	@GetMapping("/crop")
//	public ResponseEntity<String> getCropRecommendation() {
//		return new ResponseEntity<>(farmoService.getCrop(),HttpStatus.OK);
//	}
//	
//	@GetMapping("/climate")
//	public ResponseEntity<String> getClimate() {
//
//		return new ResponseEntity<>(farmoService.fetchClimate(),HttpStatus.OK);
//	}
//
//	@GetMapping("/climate/{lat}/{lon}")
//	public ResponseEntity<String> getClimateForLatLon(@PathVariable("lat") String lat, @PathVariable("lon") String lon) {
//
//		return new ResponseEntity<>(farmoService.fetchClimate(),HttpStatus.OK);
//	}
//
//	//get the fields of interest from the weather API, internally sets the POJO
//	@GetMapping("/parseJson/{lat}/{lon}")
//	public ResponseEntity<ApiResponse> getJson(@PathVariable("lat") String lat, @PathVariable("lon") String lon) throws FileNotFoundException {
//
//		return new ResponseEntity<>(farmoService.parseJson(lat, lon),HttpStatus.OK);
//	}
//
//
////	@GetMapping("/parseJson/amazon")
////	public ResponseEntity<AmazonObjList> getJsonAmazon() throws FileNotFoundException {
////
////		return new ResponseEntity<>(farmoService.parseAmazonProd(""),HttpStatus.OK);
////	}
//
	@GetMapping("/search")
	public ResponseEntity<String> getSearchList() {
		return new ResponseEntity<>(farmoService.getAmazonProducts("pesticide for crop"),HttpStatus.OK);
	}
//
//	@GetMapping("/disease")
//	public ResponseEntity<String> getPredictedDisease() {
//		return new ResponseEntity<>(farmoService.getPossibleDisease(farmoService.getCrop()),HttpStatus.OK);
//	}

}
