package com.valcon.inventory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import com.valcon.invoicing.security.AbstractWebSecurityConfig;

@Configuration
public class WebSecurityConfig extends AbstractWebSecurityConfig {

	@Override
	public void configure(final WebSecurity web) {
		// execute the web security for all requests as the REST api can't be matched by
		// simple mapper
		// because the "/api/inv1" is achieved using servlet path
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		// the request path has to be matched without servlet path prefix
		http.authorizeRequests().antMatchers( //
				"/actuator/**", //
				"/swagger-ui/**", //
				"/swagger-ui.html", //
				"/api-docs/**") //
				.permitAll().anyRequest().authenticated();
	}

}
