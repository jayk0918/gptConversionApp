package com.jayk0918.www.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PapagoTranslateService {
	
	private final UtilService utilService;
	
	private final String papagoClientId = System.getProperty("PapagoClientId");
	private final String papagoClientSecret = System.getProperty("PapagoClientSecret");
	private final String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
	
	public String doTranslate(String question, final String detectLanguage, boolean receivedAnswer) {
        try {
            question = URLEncoder.encode(question, "UTF-8");
            log.info(question);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = utilService.setRequestHeader(papagoClientId, papagoClientSecret);
        
        final String responseBody = translateQuestion(apiURL, requestHeaders, question, detectLanguage, receivedAnswer);
        log.info(responseBody);
        
        String result = "";
        try {
        	 // JSONParser로 JSONObject로 변환
            JSONParser parser = new JSONParser();
        	JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
            JSONObject resultObject = (JSONObject) ((JSONObject) jsonObject.get("message")).get("result");
            result = (String) resultObject.get("translatedText");
            log.info(result);
		} catch (ParseException e) {
			log.info(e.toString());
		}
        return result;
	}
	
	private String translateQuestion(final String apiUrl, final Map<String, String> requestHeaders, final String question, final String detectLanguage, boolean receivedAnswer){
	    HttpURLConnection con = utilService.connect(apiUrl);
	    
	    final String postParams = defineParam(question, detectLanguage, receivedAnswer);
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
	            return utilService.readBody(con.getInputStream());
	        } else {  // 에러 응답
	            return utilService.readBody(con.getErrorStream());
	        }
	    } catch (IOException e) {
	        throw new RuntimeException("API 요청과 응답 실패", e);
	    } finally {
	        con.disconnect();
	    }
	}
	
	public String defineParam(final String question, final String detectLanguage, boolean receivedAnswer) {
		if(receivedAnswer == false) {
			String postParams = "source="+detectLanguage+"&target=en&text="+question; //원본언어: <언어 감지> -> 목적언어: 영어 (en)
			return postParams;
	    }else {
	    	String postParams = "source=en&target="+detectLanguage+"&text="+question; //원본언어: <언어 감지> -> 목적언어: 영어 (en)
	    	return postParams;
	    }
	}
}