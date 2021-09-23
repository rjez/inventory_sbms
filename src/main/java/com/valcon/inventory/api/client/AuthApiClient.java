package com.valcon.inventory.api.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.valcon.inventory.dto.UserDTO;

/**
 * @author rjez
 *
 */
@FeignClient(name = "authApi", url = "${service.endpoint.users}")
public interface AuthApiClient {

	@RequestMapping(method = RequestMethod.GET, value = "${service.method.findByIds}")
	UserDTO[] getUsersByIds(@RequestParam("id") List<Long> ids);
}
