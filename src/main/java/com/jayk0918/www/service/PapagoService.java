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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayk0918.www.config.PapagoProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PapagoService {
	
	private final PapagoProperties papagoProperties;
	
	public PapagoService(PapagoProperties papagoProperties) {
		this.papagoProperties = papagoProperties;
	}

	//TO-DO : Method 분리 예정
	public String doTranslate(String question) {
        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        try {
            question = URLEncoder.encode(question, "ISO-8859-1");
            log.info(question);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", papagoProperties.getId());
        requestHeaders.put("X-Naver-Client-Secret", papagoProperties.getSecret());

        String responseBody = post(apiURL, requestHeaders, question);

        System.out.println(responseBody);
        return responseBody;
	}

	private String post(String apiUrl, Map<String, String> requestHeaders, String text){
	    HttpURLConnection con = connect(apiUrl);
	    String postParams = "source=ko&target=en&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
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