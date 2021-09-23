package com.valcon.inventory.api.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.valcon.invoicing.security.UsernamePwdAuthToken;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author rjez
 *
 */
@Component
public class FeignClientInterceptor implements RequestInterceptor {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_TYPE = "Bearer";

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header(AUTHORIZATION_HEADER, getAuthBearerToken());
	}

	public static String getAuthBearerToken() {
		return String.format("%s %s", TOKEN_TYPE, getAuthorizationToken());
	}

	private static String getAuthorizationToken() {
		String token = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getClass().isAssignableFrom(UsernamePwdAuthToken.class)) {
			UsernamePwdAuthToken jwtAuthentication = (UsernamePwdAuthToken) authentication;
			token = jwtAuthentication.getToken();
		}
		return token;
	}
}