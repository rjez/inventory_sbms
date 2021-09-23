package com.valcon.inventory.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valcon.inventory.api.client.AuthApiClient;
import com.valcon.inventory.dto.UserDTO;

/**
 * @author rjez
 *
 */
@Service
public class UserDataProviderImpl implements UserDataProvider {

	final static Logger LOG = LoggerFactory.getLogger(UserDataProviderImpl.class);

	@Autowired
	private AuthApiClient authApiClient;

	@Override
	public Map<Long, UserDTO> loadUsers(List<Long> userIds, String authorizationHeaderValue) {
		UserDTO[] response;
		if (userIds == null || userIds.isEmpty()) {
			response = new UserDTO[0];
		} else {
			response = authApiClient.getUsersByIds(userIds);
		}
		return Arrays.stream(response).collect(Collectors.toMap(UserDTO::getId, Function.identity()));
	}
}
