package com.valcon.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.valcon.inventory.api.client.FeignClientInterceptor;

/**
 * @author rjez
 *
 */
@Configuration
public class FeignConfig {

	@Bean
	public FeignClientInterceptor basicAuthRequestInterceptor() {
		return new FeignClientInterceptor();
	}
}