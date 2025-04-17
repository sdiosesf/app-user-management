package com.services.application.usermanagement.security.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import com.services.application.usermanagement.model.api.UserResponse;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public interface JWTService {

	Map <String, Object> create(Authentication auth) throws IOException;

	boolean validate(String token);

	Claims getClaims(String token);

	String getUsername(String token);

	Collection<GrantedAuthority> getRoles(String token) throws IOException;

	String resolve(String token);

	UserResponse getUserResponse(String token) throws IOException;
}
