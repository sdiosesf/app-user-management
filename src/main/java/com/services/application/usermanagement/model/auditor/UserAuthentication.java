package com.services.application.usermanagement.model.auditor;

import java.util.Collection;
import com.services.application.usermanagement.model.api.UserResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


public class UserAuthentication extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private UserResponse userResponse;

	public UserAuthentication(Object principal, Object credentials) {
		super(principal, credentials);
	}

	public UserAuthentication(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities, UserResponse userResponse) {
		super(principal, credentials, authorities);
		this.userResponse = userResponse;
	}

	public UserResponse getUserResponse() {
		return userResponse;
	}

	public void setUserResponse(UserResponse userResponse) {
		this.userResponse = userResponse;
	}
}