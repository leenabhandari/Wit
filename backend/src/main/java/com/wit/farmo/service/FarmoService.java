package com.wit.farmo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.wit.farmo.model.AmazonObj;
import com.wit.farmo.model.AmazonObjList;
import com.wit.farmo.model.ApiResponse;
import com.wit.farmo.model.FarmoRequest;
import com.wit.farmo.model.FinalResponse;
import com.wit.farmo.model.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FarmoService {
	
	@Autowired
	AutoAIService autoAIService;
	
	@Autowired
	WeatherService weatherService;
	
	@Autowired
	AmazonSearchService amazonSearchService;
	
	@Autowired
	AmazonProductService amazonProductService;
	
	@Autowired
	ProductSearchService productSearchService;
	
	
	@Autowired
	RefDataService refDataService;
	
	
	static List<String> crops,diseaseList;
	
	static Map<String, List<String>> diseases;
	
	@PostConstruct
	void inti() {
		
		diseaseList = refDataService.getDiseaseList();

		crops = refDataService.getCropList();
		
		diseases = new HashMap<String, List<String>>();
		diseases.put("rice", Arrays.asList("Brown spot","Cold injury","Pecky rice","Water-mold","salt damage","Rice tungro"));
		diseases.put("maize", Arrays.asList("Charcoal Rot","Common rust","Boil smut","leaf blight","soft rot"));
	}
	
	
	public FinalResponse getRecommendation(FarmoRequest request) {
		
		ApiResponse weatherResponse = parseJson(request.getLat().toString(), request.getLon().toString());
		
		double temp = weatherResponse.getList().get(0).getMain().getTempMin();
		double humidity = weatherResponse.getList().get(0).getMain().getHumidity();
		double rainfall = weatherResponse.getList().get(0).getRain().getH();
		
		String payload = "{\r\n"
				+ "    \"input_data\": [\r\n"
				+ "            {\r\n"
				+ "                \"fields\": [\"N\",\"P\",\"K\",\"temperature\",\"humidity\",\"ph\",\"rainfall\"], \r\n"
				+ "                \"values\": [["+request.getSoil().getN()+","+request.getSoil().getP()+","+request.getSoil().getK()+","
				+ 					temp + "," + humidity + "," + request.getSoil().getPh() + "," + rainfall + "]]\r\n"
				+ "            }\r\n"
				+ "        ]\r\n"
				+ "}";
		String cropString = getResult(payload);
		
		cropString = (!cropString.equals("maize") && !cropString.equals("rice"))?"rice":cropString;
		
		log.info(cropString);
		
		String payloadD = "{\r\n"
				+ "    \"input_data\": [\r\n"
				+ "            {\r\n"
				+ "                \"fields\": [\"N\",\"P\",\"K\",\"temperature\",\"humidity\",\"ph\",\"rainfall\",\"label\"], \r\n"
				+ "                \"values\": [["+request.getSoil().getN()+","+request.getSoil().getP()+","+request.getSoil().getK()+","
				+ 					temp + "," + humidity + "," + request.getSoil().getPh() + "," + rainfall + "," + cropString+"]]\r\n"
				+ "            }\r\n"
				+ "        ]\r\n"
				+ "}";
		String diseaseString = getDisease(payloadD);
		
		log.info(diseaseString);
		
		
		FinalResponse finalResponse = new FinalResponse();
		finalResponse.setCrops(cropString);
		List<Product> products = new ArrayList<>();
		
			String jsonProdString = getAmazonProducts("pesticides for " + cropString );
			addToList(jsonProdString, products, cropString);
			String jsonFerString = productSearchService.getFertilizerString();
			addToList(jsonFerString, products, cropString);
		
		finalResponse.setProducts(products);
		finalResponse.setDiseases(diseaseString);
		return finalResponse;
		
	}
	
	void addToList(String json, List<Product> products, String cropString) {
		try {
			AmazonObjList amazonObjs = parseAmazonProd(json);
			int num = 0;
			for(AmazonObj obj : amazonObjs.getProdList()) {
				products.add(convertToProduct(obj, cropString));
				num++;
				if(num == 3)break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	Product convertToProduct(AmazonObj obj, String cropString) {
		Product product = new Product();
		product.setDescriptionString(obj.getSubtitle());
		product.setTitleString(obj.getTitle());
		product.setImgString(obj.getImageUrl());
		String priceString = obj.getPrice();
		priceString = priceString.replaceAll("[^0-9]", "");
		product.setPriceLong(Double.valueOf(priceString)/100);
		product.setUrlString(obj.getDetailPageURL());
		product.setCropString(cropString);
		return product;
	}

	public String getResult(String payload) {
		//DO NOT uncomment for testing
		String resultString = autoAIService.getCropRecommendation(payload);
		resultString = resultString.toLowerCase();
		for(String cropString : crops) {
			if(resultString.contains(cropString)) {
				return cropString;
			}
		}
		return "rice";
	}
	
	public String getDisease(String payload) {
		//DO NOT uncomment for testing
		String resultString = autoAIService.getDiseasePrediction(payload);
		for(String diString : diseaseList) {
			if(resultString.contains(diString)) {
				return diString;
			}
		}
		return "blast";
		
	}
	
	public String getCrop() {
		return "rice";
	}
	
	public String getAmazonProducts(String keyString) {
		//DO NOT uncomment following line unless absolutely necessary
		log.info(keyString);
		return amazonProductService.fetchAmazonProducts(keyString); //"fertilizers for rice crops"
		
		//return productSearchService.getPesticideString();
	}
	
	public String getPossibleDisease(String crop) {
		int index = (int)(Math.random() * diseases.get(crop).size());
		return diseases.get(crop).get(index);
	}
	
	public String getSearchResult() {
		return amazonSearchService.getSearchResults("rice");
	}
	
	public String fetchClimate() {
		return weatherService.getWeatherData(14.6819, 77.6006);
	}

	public String fetchClimateForLatLon(String latStr, String lonStr){
		double lat = Double.parseDouble(latStr);
		double lon = Double.parseDouble(lonStr);
		return weatherService.getWeatherData(lat, lon);

	}

	
	   public ApiResponse parseJson(String lat, String lon)  {
	        Gson gson = new Gson();
	        String jsonStr = fetchClimateForLatLon(lat,lon);
	        ApiResponse response = gson.fromJson(jsonStr, ApiResponse.class);
	        response.getList().stream().forEach(x -> System.out.println(x.getMain().getHumidity() + " " + x.getRain().getH()));
	        return response;
	    }


	public String fetchAmazonList(){
		return null;
	}
	
	
	public AmazonObjList parseAmazonProd(String jsonStr) {
        Gson gson = new Gson();
//        File file = ResourceUtils.getFile("classpath:temp2.json");
//        Reader reader = new FileReader(file);
        //String jsonStr  = productSearchService.getFertilizerString();
        StringBuilder sb = new StringBuilder(jsonStr);
        sb.insert(0,"{ \"prodList\":");
        sb.append("}");
        AmazonObjList amazonObjList = gson.fromJson(sb.toString(), AmazonObjList.class);
        amazonObjList.getProdList().stream().forEach(x -> System.out.println(x.getAsin()));
        return amazonObjList;
    }

}
