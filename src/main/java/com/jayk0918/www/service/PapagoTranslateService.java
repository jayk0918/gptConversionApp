package com.jayk0918.www.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PapagoTranslateService {
	private final String papagoClientId = System.getProperty("PapagoClientId");
	private final String papagoClientSecret = System.getProperty("PapagoClientSecret");
	private final String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
	
	private final PapagoDetectService papagoDetectService;
	
	
	public String doTranslate(String question) {
        try {
            question = URLEncoder.encode(question, "UTF-8");
            log.info(question);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", papagoClientId);
        requestHeaders.put("X-Naver-Client-Secret", papagoClientSecret);

        String responseBody = post(apiURL, requestHeaders, question);

        log.info(responseBody);
        return responseBody;
	}
	
	
	private String post(String apiUrl, Map<String, String> requestHeaders, String question){
	    HttpURLConnection con = connect(apiUrl);
	    String postParams = "source=ko&target=en&text=" + question; //원본언어: <언어 감지> -> 목적언어: 영어 (en)
	    log.info(postParams);
	    
	    try {
	        con.setRequestMethod("POST");
	        for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
	            con.setRequestProperty(header.getKey(), header.getValue());
	        }
	
	        con.setDoOutput(true);
	        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
	            wr.write(postParams.getBytes());
	            wr.flush();
	        }
	
	        int responseCode = con.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
	            return readBody(con.getInputStream());
	        } else {  // 에러 응답
	            return readBody(con.getErrorStream());
	        }
	    } catch (IOException e) {
	        throw new RuntimeException("API 요청과 응답 실패", e);
	    } finally {
	        con.disconnect();
	    }
	}
	
	private HttpURLConnection connect(String apiUrl){
	    try {
	        URL url = new URL(apiUrl);
	        return (HttpURLConnection)url.openConnection();
	    } catch (MalformedURLException e) {
	        throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
	    } catch (IOException e) {
	        throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
	    }
	}
	
	private String readBody(InputStream body){
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
}