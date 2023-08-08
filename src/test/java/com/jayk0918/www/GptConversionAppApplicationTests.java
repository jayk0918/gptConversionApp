package com.jayk0918.www;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jayk0918.www.service.ChatService;
import com.jayk0918.www.service.PapagoDetectService;
import com.jayk0918.www.service.PapagoTranslateService;
import com.jayk0918.www.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class GptConversionAppApplicationTests {
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	PapagoDetectService papagoDetectService;
	
	@Autowired
	PapagoTranslateService papagoTranslateService;
	
	@Autowired
	UtilService utilService;
	

	@Test
	@DisplayName("Response test")
	void responseTest() {
		final String testInput = "Where is the capital of Korea?";
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
	
	@Test
	@DisplayName("Translate test false")
	void translateTestFalse() {
		final String testInput = "한글";
		boolean receivedAnswer = false;
		String detectLanguage = papagoDetectService.detectLanguange(testInput);
		String result = papagoTranslateService.doTranslate(testInput, detectLanguage, receivedAnswer);
		log.info(result);
	}
	
	@Test
	@DisplayName("Translate test true")
	void translateTestTrue() {
		final String testInput = "hello";
		boolean receivedAnswer = true;
		String detectLanguage = papagoDetectService.detectLanguange(testInput);
		String result = papagoTranslateService.doTranslate(testInput, detectLanguage, receivedAnswer);
		log.info(result);
	}
	
	@Test
	@DisplayName("DefineParam test false")
	void defineParamTestFalse() {
		final String testInput = "한글";
		boolean receivedAnswer = false;
		String detectLanguage = papagoDetectService.detectLanguange(testInput);
		String result = papagoTranslateService.defineParam(testInput, detectLanguage, receivedAnswer);
		log.info(result);
	}
	
	@Test
	@DisplayName("DefineParam test true")
	void defineParamTestTrue() {
		final String testInput = "한글";
		boolean receivedAnswer = true;
		String detectLanguage = papagoDetectService.detectLanguange(testInput);
		String result = papagoTranslateService.defineParam(testInput, detectLanguage, receivedAnswer);
		log.info(result);
	}
	
	@Test
	@DisplayName("DetectEnglish test")
	void detectEnglish() {
		final String testInput = "hello";
		String detectLanguage = papagoDetectService.detectLanguange(testInput);
		String result = utilService.detectEnglish(testInput, detectLanguage);
		log.info(result);
	}
	
}
