package com.jayk0918.www;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.jayk0918.www.controller.ChatController;
import com.jayk0918.www.service.PapagoDetectService;

@ExtendWith(MockitoExtension.class)
public class DetectLanguageTest {
	
	@InjectMocks
	private ChatController chatController;
	
	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
	}
	
	private MockMvc mockMvc;
	
	@Mock
	private PapagoDetectService papagoDetectService;
	
	@DisplayName("detectLanguage")
	@Test
	void detectLanguage() throws Exception {
		// given
	    String request = "Where is the capital of Korea?";
	    //String response = papagoDetectService.detectLanguange(request);
	    
	    // when
	    ResultActions resultActions = mockMvc.perform(
	        MockMvcRequestBuilders.post("/api/v1/chat-gpt")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(request)
	    );
	    
	    // then
	    MvcResult mvcResult = resultActions.andExpect(status().isOk())
	    		.andReturn();
	        
	}


	
	
	
	
	
	
	
	

}
