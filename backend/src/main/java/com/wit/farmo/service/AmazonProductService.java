package com.wit.farmo.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wit.farmo.exception.ApiException;

@Service
public class AmazonProductService {
	
	@Value("${amazon.key}")
	private String key;
	
	public String fetchAmazonProducts(String keyword) {
		String keywordString =  URLEncoder.encode(keyword, StandardCharsets.UTF_8);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://amazon-price1.p.rapidapi.com/search?marketplace=IN&keywords=" + keywordString))
				.header("x-rapidapi-key", key)
				.header("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
			return response.body();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		throw new ApiException("Amazon search failed");
	}

}
