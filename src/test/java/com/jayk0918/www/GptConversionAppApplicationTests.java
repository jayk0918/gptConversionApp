package com.jayk0918.www;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jayk0918.www.service.ChatService;
import com.jayk0918.www.service.PapagoDetectService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class GptConversionAppApplicationTests {
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	PapagoDetectService papagoDetectService;
	
	@Test
	@DisplayName("Response test")
	void responseTest() {
		final String testInput = "hello";
		String result = "";
		result = chatService.getChatResponse(testInput);
		log.info(result);
	}
	
	@Test
	@DisplayName("Detection test")
	void detectionTest() {
		final String testInput = "hello";
		String result = papagoDetectService.detectLanguange(testInput);
		log.info(result);
	}
	
	
	

}
