package com.jayk0918.www.record;

public record ChatGptResponseUsage(
	    int prompt_tokens,
	    int completion_tokens,
	    int total_tokens
	) {
	
}