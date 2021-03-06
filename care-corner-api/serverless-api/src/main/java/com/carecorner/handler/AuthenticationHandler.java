package com.carecorner.handler;

import com.carecorner.gateway.ApiGatewayResponse;
import com.carecorner.gateway.Response;

import com.carecorner.dao.UserDao;
import com.carecorner.pojo.User;

import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.type.TypeReference;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AuthenticationHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private final UserDao userDao = UserDao.INSTANCE;
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.debug("Authentication Handler received: {}", input);

		Integer userId = 0;
		int statusCode = 401;
		try {
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			logger.debug("Params: {}", body);

			String username = body.get("username").asText();
			String password = body.get("password").asText();

			logger.debug("Username: {}", username);
			logger.debug("Password: {}", password);
			List<User> users = userDao.findByUsername(username);
			logger.debug("Users: {}", users.toString());
			if (users.size() > 0) {
				User user = users.get(0);
				if (user.getPassword().equals(password)) {
					// authenticated
					statusCode = 200;
					userId = user.getId();
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();

			// send the error response back
			Response responseBody = new Response("Error in resource: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(statusCode)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "Care Corner"))
					.build();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user-id", userId);
		Response responseBody = new Response("Authenticated", map);
		ApiGatewayResponse apiResponse = ApiGatewayResponse.builder()
				.setStatusCode(statusCode)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("X-Powered-By", "Care Corner"))
				.build();
		logger.debug("Returning: {}", apiResponse.getBody());
		return apiResponse;
	}
}

