package com.mamoori.mamooriback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	@Bean
	public RestTemplate restTemplate(){ // 다른 서버의 API endpoint를 호출할 때 RestTemplate 사용
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate;
	}
}
