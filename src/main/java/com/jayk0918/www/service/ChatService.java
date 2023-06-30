package com.jayk0918.www.service;

import org.springframework.stereotype.Service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService{

    private ChatgptService chatgptService;

    public String getChatResponse(String prompt) {
            // ChatGPT 에게 질문을 던집니다.
            return chatgptService.sendMessage(prompt);
    }
}
