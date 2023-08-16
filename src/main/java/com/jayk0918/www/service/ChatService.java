package com.jayk0918.www.service;

import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayk0918.www.record.ChatGptRequest;
import com.jayk0918.www.record.ChatGptResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService{
	
	private final UtilService utilService;
	private final PapagoTranslateService papagoTranslateService;
	
	private final String openAiKeys = System.getProperty("OpenAIKeys"); 
	private final String apiModel = "text-davinci-003";
	private final String openAiUri = "https://api.openai.com/v1/completions";
	
    public String getChatResponse(String question){
        // ChatGPT 에게 질문을 던집니다.
    	ObjectMapper mapper = new ObjectMapper();
        ChatGptRequest chatGptRequest = new ChatGptRequest(apiModel, question, 1, 100);
        String input = "";
		try {
			input = mapper.writeValueAsString(chatGptRequest);
		} catch (JsonProcessingException e) {
			log.info(e.toString());
		}
		
		// httpRequest
		HttpResponse<String> response = utilService.doHttpRequest(input, openAiUri, openAiKeys);
		
		// response에 따른 처리
        if (response.statusCode() == 200) {
            ChatGptResponse chatGptResponse = null;
			try {
				chatGptResponse = mapper.readValue(response.body(), ChatGptResponse.class);
			} catch (JsonMappingException e) {
				log.info(e.toString());
			} catch (JsonProcessingException e) {
				log.info(e.toString());
			}
			
			// TO-DO : StringUtil 변환
            final String answer = chatGptResponse.choices()[chatGptResponse.choices().length-1].text();
            if (!answer.isEmpty()) {
            	log.info(answer.replace("\n", "").trim());
            }
            return answer;
        } else {
        	log.info(response.body());
            return response.body();
        }
    }
    
    public String checkResponse(final String question, final String detectLanguage) {
		//openAi 응답여부(번역 source 파라미터 결정)
    	boolean receivedAnswer = false;
    	final String translatedQuestion = papagoTranslateService.doTranslate(question, detectLanguage, receivedAnswer);
    	log.info(translatedQuestion);
    	
    	final String respondedAnswer = getChatResponse(translatedQuestion);
    	log.info(respondedAnswer);
    	
    	//openAi 응답을 받을 시 boolean 변경
    	if(respondedAnswer != null) {
    		receivedAnswer = true;
    	}
    	
    	final String translatedAnswer = papagoTranslateService.doTranslate(respondedAnswer, detectLanguage, receivedAnswer);
    	log.info(translatedAnswer);
    	
    	return translatedAnswer;
	}
    
    
    
}
