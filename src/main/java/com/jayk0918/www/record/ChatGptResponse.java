package com.jayk0918.www.record;

public record ChatGptResponse(
	    String id,
	    String object,
	    int created,
	    String model,
	    ChatGptResponseChoice[] choices,
	    ChatGptResponseUsage usage
	) {
	}
