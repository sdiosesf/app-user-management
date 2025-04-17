package com.services.application.usermanagement.security.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.services.application.usermanagement.model.api.UserResponse;
import com.services.application.usermanagement.model.dto.UserDto;
import com.services.application.usermanagement.security.service.JWTService;
import com.services.application.usermanagement.security.service.SimpleGrantedAuthorityMixin;
import com.services.application.usermanagement.service.UserService;
import com.services.application.usermanagement.util.Constants;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JWTServiceImpl implements JWTService {

	protected static final byte[] SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();

	public static final long EXPIRATION_DATE = 36000000L;

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String HEADER_STRING = "Authorization";

	public static final String AUTHORITIES_STRING = "authorities";

	public static final String USER_STRING = "user";

	private final UserService userService;

	public JWTServiceImpl(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public Map <String, Object> create(Authentication auth) throws IOException {
		Map<String, Object> result = new HashMap<>();
		String username = ((User) auth.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		
		UserDto userDto = userService.findByEmail(username);
		
		if (userDto == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have access.");
		}
		
		Claims claims = Jwts.claims();
		claims.put(AUTHORITIES_STRING, new ObjectMapper().writeValueAsString(roles));
		claims.put(USER_STRING, new ObjectMapper().writeValueAsString(userDto));
		
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(Keys.hmacShaKeyFor(SECRET), SignatureAlgorithm.HS512)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
				.compact();
		
		result.put(Constants.KEY_TOKEN, token);
		result.put(USER_STRING, userDto);

		userService.updateLastLogin(userDto.getId());
		
		return result;
	}
	
	@Override
	public boolean validate(String token) {
		boolean result = true;
		try {
			getClaims(token);
		}catch(JwtException | IllegalArgumentException ex) {
			result = false;
		}
		return result;
	}
	
	@Override
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(SECRET))
			.build()
			.parseClaimsJws(resolve(token))
			.getBody();
	}
	
	@Override
	public Collection<GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get(AUTHORITIES_STRING);
		return Arrays.asList(new ObjectMapper()
			.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
			.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
	}

	@Override
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	@Override
	public String resolve(String token) {
		if (token != null && token.startsWith(TOKEN_PREFIX)) {
			return token.replace(TOKEN_PREFIX, "");
		} else {
			return null;
		}
	}

	@Override
	public UserResponse getUserResponse(String token) throws IOException {
		Object user = getClaims(token).get(USER_STRING);
		return new ObjectMapper().readValue(user.toString(), UserResponse.class);
	}	
}
