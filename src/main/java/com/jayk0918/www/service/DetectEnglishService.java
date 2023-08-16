package com.jayk0918.www.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectEnglishService {
	
	private final ChatService chatService;
	
	public String detectEnglish(String question, String detectLanguage) {
		// 영어 질의일 경우 바로 질의로 넘어감
    	if(detectLanguage.equals("en")) {
    		final String answer = chatService.getChatResponse(question);
    		log.info(answer);
    		return answer;
    	}else {
    		final String answer = chatService.checkResponse(question, detectLanguage);
        	log.info(answer);
        	return answer;
    	}
	}
}
