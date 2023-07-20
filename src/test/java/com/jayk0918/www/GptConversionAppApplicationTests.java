package com.jayk0918.www;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class GptConversionAppApplicationTests {
	
	@Test
	public void contextLoads() throws Exception {
		log.info(System.getProperty("OpenAIKeys"));
		log.info(System.getProperty("PapagoClientId"));
		log.info(System.getProperty("PapagoClientSecret"));
	}

}
