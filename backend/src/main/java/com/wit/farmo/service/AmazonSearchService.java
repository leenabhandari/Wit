package com.wit.farmo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wit.farmo.exception.ApiException;

//unused
@Service
public class AmazonSearchService {
	
	private static String accessKeyString = "";
	
	@Value("${amazon.secret.key}")
	private String secretKeyString;
	
	 String HOST = "webservices.amazon.com";
     String region = "us-east-1";
     String path = "/paapi5/searchitems";

    public String getSearchResults(String keyword) {

        // Sample SearchItems request. You can also use scratchpad tool (UI) to test a request and then use JSON Payload value here. Scratchpad link: https://webservices.amazon.com/paapi5/scratchpad/index.html
        // Put your Partner tag (Store/Tracking id) in place of Partner tag
        String requestPayload = "{\"Keywords\":\"rice\","
                + "\"PartnerTag\":\"xxx-zz\",\"PartnerType\":\"Associates\","
                + "\"SearchIndex\":\"All\","
                +"\"Resources\": [\"Images.Primary.Small\",\"ItemInfo.Title\",\"Offers.Listings.Price\"]}";

        TreeMap<String, String> headers = new TreeMap<String, String>();
        headers.put("host", HOST);
        headers.put("content-type", "application/json; charset=utf-8");
        // x-amz-target is value specific to your version and operation. For version 1's SearchItems it'll be com.amazon.paapi5.v1.ProductAdvertisingAPIv1.SearchItems
        headers.put("x-amz-target", "com.amazon.paapi5.v1.ProductAdvertisingAPIv1.SearchItems");
        headers.put("content-encoding", "amz-1.0");

        // Put your Access Key in place of <ACCESS_KEY> and Secret Key in place of <SECRET_KEY> in double quotes
        AWSV4Auth awsv4Auth = new AWSV4Auth.Builder(accessKeyString, secretKeyString)
            .path(path)
            .region(region)
            .service("ProductAdvertisingAPI")
            .httpMethodName("POST")
            .headers(headers)
            .payload(requestPayload)
            .build();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("https://" + HOST + path);
        try {
			httpPost.setEntity(new StringEntity(requestPayload));
			// Signing
	        Map<String, String> header = awsv4Auth.getHeaders();
	        for (Map.Entry<String, String> entrySet : header.entrySet()) {
	            httpPost.addHeader(entrySet.getKey(), entrySet.getValue());
	            // Print headers by un-commenting following line
	            //System.out.println("Key: " + entrySet.getKey() + " Value: " + entrySet.getValue());
	        }

	        HttpResponse response = client.execute(httpPost);
	        HttpEntity entity = response.getEntity();
	        String jsonResponse = EntityUtils.toString(entity, "UTF-8");
	        int statusCode = response.getStatusLine().getStatusCode();
	        System.out.println(jsonResponse);
	        if(statusCode == 200) {
	            System.out.println("Successfully received response from Product Advertising API.");
	            return jsonResponse;
	        } else {
	            JSONObject json = new JSONObject(jsonResponse);
	            if(json.has("Errors")) {
	                JSONArray errorArray = json.getJSONArray("Errors");
	                for(int i = 0; i < errorArray.length(); i++) {
	                    JSONObject e = errorArray.getJSONObject(i);
	                    System.out.println("Error Code: "+e.get("Code")+", Message: "+e.get("Message"));
	                }
	            } else {
	                System.out.println("Error Code: InternalFailure, Message: The request processing has failed because of an unknown error, exception or failure. Please retry again.");
	            }
	        }
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        throw new ApiException("Amazon search failed");
    }
}