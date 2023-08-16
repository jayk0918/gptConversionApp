package com.jayk0918.www.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jayk0918.www.service.DetectEnglishService;
import com.jayk0918.www.service.PapagoDetectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-gpt")
public class ChatController {
	
	private final PapagoDetectService papagoDetectService;
	private final DetectEnglishService detectEnglishService;
	
    //chat-gpt 와 간단한 채팅 서비스 소스
    @PostMapping("")
    public String test(@RequestBody final String question){
    	log.info(question);
    	
    	//question 언어 감지 & 번역
    	final String detectLanguage = papagoDetectService.detectLanguange(question);
    	log.info(detectLanguage);
    	final String answer = detectEnglishService.detectEnglish(question, detectLanguage);
        return answer;
    }
}
