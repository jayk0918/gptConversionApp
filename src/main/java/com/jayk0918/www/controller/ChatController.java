package com.jayk0918.www.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jayk0918.www.service.ChatService;
import com.jayk0918.www.service.PapagoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-gpt")
public class ChatController {
	
    private final static Logger log = LoggerFactory.getLogger(ChatController.class);

	@Autowired
    private ChatService chatService;
	
	@Autowired
	private PapagoService papagoService;
	
    //chat-gpt 와 간단한 채팅 서비스 소스
    @PostMapping("")
    public String test(@RequestBody String question) throws IOException, InterruptedException{
    	//TO-DO : validation check
    	log.info(question);
    	
    	//question 언어 감지 & 번역
    	papagoService.doTranslate(question);
    	
    	String result = chatService.getChatResponse(question);
    	
    	log.info(result);
        return result;
    }
    
    
    
    
}
