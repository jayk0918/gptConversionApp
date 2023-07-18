package com.jayk0918.www.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayk0918.www.config.OpenAIProperties;
import com.jayk0918.www.record.ChatGptRequest;
import com.jayk0918.www.record.ChatGptResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService{
	
	private final OpenAIProperties openAIProperties;
	
	// TO-DO : HttpRequest Method 분리
    public String getChatResponse(String prompt) throws IOException, InterruptedException {
        // ChatGPT 에게 질문을 던집니다.
    	ObjectMapper mapper = new ObjectMapper();
        ChatGptRequest chatGptRequest = new ChatGptRequest("text-davinci-001", prompt, 1, 100);
        String input = mapper.writeValueAsString(chatGptRequest);
        
        log.info(openAIProperties.getKeys());
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.openai.com/v1/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + openAIProperties.getKeys())
            .POST(HttpRequest.BodyPublishers.ofString(input))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ChatGptResponse chatGptResponse = mapper.readValue(response.body(), ChatGptResponse.class);
            String answer = chatGptResponse.choices()[chatGptResponse.choices().length-1].text();
            // TO-DO : StringUtil 변환
            if (!answer.isEmpty()) {
            	log.info(answer.replace("\n", "").trim());
            }
            return answer;
        } else {
        	log.info(response.body());
            return response.body();
        }
		
    }
}
