package com.services.application.usermanagement.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.services.application.usermanagement.model.api.UserSaveRequest;
import com.services.application.usermanagement.model.auditor.UserAuthentication;
import com.services.application.usermanagement.security.service.JWTService;
import com.services.application.usermanagement.security.service.impl.JWTServiceImpl;
import com.services.application.usermanagement.util.Constants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  private final JWTService jwtService;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
	this.authenticationManager = authenticationManager;
	setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
	this.jwtService = jwtService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	  throws AuthenticationException {
	String username = null;
	String password = null;

	try {
		UserSaveRequest userSaveRequest = new ObjectMapper().readValue(request.getInputStream(), UserSaveRequest.class);
		username = userSaveRequest.getEmail();
		password = userSaveRequest.getPassword();
	} catch (Exception e) {
		e.printStackTrace();
	}

	UserAuthentication authentication = new UserAuthentication(username, password);
	return authenticationManager.authenticate(authentication);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain, Authentication authResult) throws IOException {
	log.info("Authentication success. {}", authResult.getName());

	Map<String, Object> result = jwtService.create(authResult);
	String token = String.valueOf(result.get(Constants.KEY_TOKEN));

	response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX + token);

	Map<String, Object> body = new HashMap<>();
	body.put(Constants.KEY_TOKEN, token);
	body.put(Constants.KEY_MESSAGE, String.format("Hello %s, you're logged in!", ((User) authResult
		.getPrincipal()).getUsername()));

	response.getWriter().write(new ObjectMapper().writeValueAsString(body));
	response.setStatus(200);
	response.setContentType(Constants.APPLICATION_JSON);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException {
	log.info("Authentication fail. {}", failed.getMessage());
	Map<String, Object> body = new HashMap<>();
	body.put(Constants.KEY_MESSAGE, Constants.Message.ERROR_INCORRECT_USERNAME_PASS);
	body.put(Constants.KEY_ERROR, failed.getMessage());

	response.getWriter().write(new ObjectMapper().writeValueAsString(body));
	response.setStatus(401);
	response.setContentType(Constants.APPLICATION_JSON);
  }
}
