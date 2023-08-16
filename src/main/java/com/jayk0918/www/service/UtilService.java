package com.jayk0918.www.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilService {
	
	public HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}
	
	public HttpResponse<String> doHttpRequest(String input, String openAiUri, String openAiKeys) {
    	HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(openAiUri))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAiKeys)
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = null;
            
    		try {
    			response = client.send(request, HttpResponse.BodyHandlers.ofString());
    		} catch (IOException e) {
    			log.info(e.toString());
    		} catch (InterruptedException e) {
    			log.info(e.toString());
    		}
    		return response;
    }
	
	public String readBody(InputStream body){
	    InputStreamReader streamReader = new InputStreamReader(body);
	
	    try (BufferedReader lineReader = new BufferedReader(streamReader)) {
	        StringBuilder responseBody = new StringBuilder();
	
	        String line;
	        while ((line = lineReader.readLine()) != null) {
	            responseBody.append(line);
	        }
	
	        return responseBody.toString();
	    } catch (IOException e) {
	        throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
	    }
	}
	
	public Map<String, String> setRequestHeader(final String id, final String secretKey) {
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", id);
		requestHeaders.put("X-Naver-Client-Secret", secretKey);
		return requestHeaders;
	}
}
