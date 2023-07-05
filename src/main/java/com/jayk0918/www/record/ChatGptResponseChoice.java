package com.jayk0918.www.record;

public record ChatGptResponseChoice(
	    String text,
	    int index,
	    Object logprobs,
	    String finish_reason
	) {
	}
