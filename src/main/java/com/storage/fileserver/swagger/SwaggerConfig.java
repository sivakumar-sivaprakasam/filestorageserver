package com.storage.fileserver.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI().openapi("3.0.2").info(new Info().title("File Storage Server API").version("v1.0"));
	}
}
