package com.services.application.usermanagement.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.services.application.usermanagement.model.auditor.UserAuthentication;
import com.services.application.usermanagement.security.service.JWTService;
import com.services.application.usermanagement.security.service.impl.JWTServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private final JWTService jwtService;

  public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
		JWTService jwtService) {
	super(authenticationManager);
	this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
	  FilterChain chain) throws IOException, ServletException {
	String header = request.getHeader(JWTServiceImpl.HEADER_STRING);

	if (!requiresAuthentication(header)) {
		chain.doFilter(request, response);
		return;
	}
	UserAuthentication authentication = null;
	if (jwtService.validate(header)) {
		authentication = new UserAuthentication(jwtService.getUsername(header), null, jwtService
				.getRoles(header), jwtService.getUserResponse(header));
	}
	SecurityContextHolder.getContext().setAuthentication(authentication);
	chain.doFilter(request, response);
}

  protected boolean requiresAuthentication(String header) {
	return header != null && header.startsWith(JWTServiceImpl.TOKEN_PREFIX);
  }
}
