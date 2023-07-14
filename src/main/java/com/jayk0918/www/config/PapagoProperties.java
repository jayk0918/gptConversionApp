package com.jayk0918.www.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("papagoclient")
public class PapagoProperties {
	private String id;
	private String secret;
}
