package com.jayk0918.www.record;

public record ChatGptRequest(String model, String prompt, int temperature, int max_tokens) {
}
