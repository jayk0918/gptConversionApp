package com.jayk0918.www.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jayk0918.www.service.ChatService;
import com.jayk0918.www.service.PapagoDetectService;
import com.jayk0918.www.service.PapagoTranslateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-gpt")
public class ChatController {
	
    private final ChatService chatService;
	private final PapagoTranslateService papagoTranslateService;
	private final PapagoDetectService papagoDetectService;
	
    //chat-gpt 와 간단한 채팅 서비스 소스
    @PostMapping("")
    public String test(@RequestBody String question){
    	log.info(question);
    	
    	//question 언어 감지 & 번역
    	String detectLanguage = papagoDetectService.detectLanguange(question);
    	log.info(detectLanguage);
    	
    	String answer = "";
    	
    	// 영어 질의일 경우 바로 질의로 넘어감
    	if(detectLanguage.equals("en")) {
    		answer = chatService.getChatResponse(question);
    		log.info(answer);
    	}else {
    		//openAi 응답여부(번역 source 파라미터 결정)
        	boolean receivedAnswer = false;
        	question = papagoTranslateService.doTranslate(question, detectLanguage, receivedAnswer);
        	log.info(question);
        	
        	answer = chatService.getChatResponse(question);
        	
        	//openAi 응답을 받을 시 boolean 변경
        	if(answer != null) {
        		receivedAnswer = true;
        	}
        	answer = papagoTranslateService.doTranslate(answer, detectLanguage, receivedAnswer);
        	log.info(answer);
    	}
    	
        return answer;
    }
    
    
    
    
}
