package com.jayk0918.www;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GptConversionAppApplication {
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GptConversionAppApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(GptConversionAppApplication.class, args);
	}

}
