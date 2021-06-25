package com.wit.farmo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.wit.farmo.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherService {

	public String getWeatherData(double lat, double lon) {
		
		HttpURLConnection tokenConnection = null;
		BufferedReader tokenBuffer = null;
		try {
		URL tokenUrl = new URL("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid=659b84ac70c14977ff632905150d2b7e");
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
		
		return jsonString.toString();
		
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
			
		}
		throw new ApiException("Weather data unavailable");
	}
}
