package com.wit.farmo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wit.farmo.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoAIService {
	
	@Value("${ibm.key.le}")
	private String produceKey;
	
	@Value("${ibm.key.su}")
	private String cropKey;
	
	@Value("${ibm.key.jm}")
	private String diseaseKey;
	
	@Value("${ibm.token.url}")
	private String tokenEndpoint;
	
	@Value("${ibm.crop.url}")
	private String cropUrl;
	
	@Value("${ibm.produce.url}")
	private String produceUrl;
	
	@Value("${ibm.disease.url}")
	private String diseaseUrl;
	
	public String getCropProduce(String payload0) {
		String payload = "{\"input_data\": [{\"fields\": [\"State_Name\",\"District_Name\",\"Crop_Year\",\"Season\",\"Crop\",\"Area\""
				+ "], \"values\": [[\"Karnataka\", \"BIJAPUR\",2003,\"Rabi\",\"Jowar\",228540]]}]}";
		log.info(payload,tokenEndpoint);
		return getJsonStringScoring(payload, produceKey, produceUrl);
	}
	
	public String getDiseasePrediction(String payload) {
		return getJsonStringScoring(payload, diseaseKey, diseaseUrl);
	}
	
	public String getCropRecommendation(String payload) {
//		String payload = "{\r\n"
//				+ "    \"input_data\": [\r\n"
//				+ "            {\r\n"
//				+ "                \"fields\": [\"N\",\"P\",\"K\",\"temperature\",\"humidity\",\"ph\",\"rainfall\"], \r\n"
//				+ "                \"values\": [[90, 42,43,20.87974371,82.00274423,6.502985292,202.9355362]]\r\n"
//				+ "            }\r\n"
//				+ "        ]\r\n"
//				+ "}";
		log.info(payload,tokenEndpoint);
		return getJsonStringScoring(payload, cropKey, cropUrl);
	}
	

	public String getJsonStringScoring(String payload, String key, String url){
		// NOTE: you must manually set API_KEY below using information retrieved from your IBM Cloud account.

				//String API_KEY = "EA_TU-xnlwrqosEjb8xTmZMnOJSRIWHsV0Unu-XSE4Hq";

				HttpURLConnection tokenConnection = null;
				HttpURLConnection scoringConnection = null;
				BufferedReader tokenBuffer = null;
				BufferedReader scoringBuffer = null;
				try {
					// Getting IAM token
					URL tokenUrl = new URL(tokenEndpoint + key);
					tokenConnection = (HttpURLConnection) tokenUrl.openConnection();
					tokenConnection.setDoInput(true);
					tokenConnection.setDoOutput(true);
					tokenConnection.setRequestMethod("POST");
					tokenConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					tokenConnection.setRequestProperty("Accept", "application/json");
					tokenBuffer = new BufferedReader(new InputStreamReader(tokenConnection.getInputStream()));
					StringBuffer jsonString = new StringBuffer();
					String line;
					while ((line = tokenBuffer.readLine()) != null) {
						jsonString.append(line);
					}
					// Scoring request
					URL scoringUrl = new URL(url);
					String iam_token = "Bearer " + jsonString.toString().split(":")[1].split("\"")[1];
					scoringConnection = (HttpURLConnection) scoringUrl.openConnection();
					scoringConnection.setDoInput(true);
					scoringConnection.setDoOutput(true);
					scoringConnection.setRequestMethod("POST");
					scoringConnection.setRequestProperty("Accept", "application/json");
					scoringConnection.setRequestProperty("Authorization", iam_token);
					scoringConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
					OutputStreamWriter writer = new OutputStreamWriter(scoringConnection.getOutputStream(), "UTF-8");

					// NOTE: manually define and pass the array(s) of values to be scored in the next line
					
					writer.write(payload);
					writer.close();

					scoringBuffer = new BufferedReader(new InputStreamReader(scoringConnection.getInputStream()));
					StringBuffer jsonStringScoring = new StringBuffer();
					String lineScoring;
					while ((lineScoring = scoringBuffer.readLine()) != null) {
						jsonStringScoring.append(lineScoring);
					}
					System.out.println(jsonStringScoring);
					return jsonStringScoring.toString();
				} catch (IOException e) {
					System.out.println("The URL is not valid.");
					System.out.println(e.getMessage());
				}
				finally {
					if (tokenConnection != null) {
						tokenConnection.disconnect();
					}
					if (tokenBuffer != null) {
						try {
							tokenBuffer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (scoringConnection != null) {
						scoringConnection.disconnect();
					}
					if (scoringBuffer != null) {
						try {
							scoringBuffer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			return "";
	}
}
