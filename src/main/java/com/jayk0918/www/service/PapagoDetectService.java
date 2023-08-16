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
public class PapagoDetectService {
	
	private final UtilService utilService;
	
	private final String papagoClientId = System.getProperty("PapagoDetectId");
	private final String papagoClientSecret = System.getProperty("PapagoDetectSecret");
	private final String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";

	public String detectLanguange(final String question) {
		String query;
		try {
			query = URLEncoder.encode(question, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("인코딩 실패", e);
		}

		Map<String, String> requestHeaders = utilService.setRequestHeader(papagoClientId, papagoClientSecret);

		String responseBody = getLanguageCode(apiURL, requestHeaders, query);
		log.info(responseBody);
		
		String result = "";
		// JSONParser로 JSONObject로 변환
        JSONParser parser = new JSONParser();
        try {
			JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
			result = (String) jsonObject.get("langCode");
			log.info(result);
		} catch (ParseException e) {
			log.info(e.toString());
		}
		return result;
	}

	private String getLanguageCode(String apiUrl, Map<String, String> requestHeaders, String text) {
		HttpURLConnection con = utilService.connect(apiUrl);
		
		String postParams = "query=" + text; // 원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
		
		try {
			con.setRequestMethod("POST");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
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
			} else { // 에러 응답
				return utilService.readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}
	
}
