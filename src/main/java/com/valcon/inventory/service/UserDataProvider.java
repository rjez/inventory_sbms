package com.valcon.inventory.service;

import java.util.List;
import java.util.Map;

import com.valcon.inventory.dto.UserDTO;

/**
 * @author rjez
 *
 */
public interface UserDataProvider {

	Map<Long, UserDTO> loadUsers(List<Long> userIds, String authorizationHeaderValue);

}
