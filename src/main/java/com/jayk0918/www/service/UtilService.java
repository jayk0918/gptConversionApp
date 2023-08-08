package com.jayk0918.www.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilService {
	
	private final ChatService chatService;
	private final PapagoTranslateService papagoTranslateService;
	
	public String detectEnglish(String question, String detectLanguage) {
		String answer = "";
		// 영어 질의일 경우 바로 질의로 넘어감
    	if(detectLanguage.equals("en")) {
    		answer = chatService.getChatResponse(question);
    		log.info(answer);
    	}else {
        	answer = chatService.checkResponse(question, detectLanguage);
        	log.info(answer);
    	}
    	return answer;
	}
}
