package com.services.application.usermanagement.security.service.impl;
import java.util.ArrayList;
import java.util.List;

import com.services.application.usermanagement.model.dto.UserDto;
import com.services.application.usermanagement.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

  private UserService userService;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	UserDto userDto = userService.findByEmail(username);
	if (userDto == null) {
		throw new UsernameNotFoundException("User " + username + " is not registered.");
	}

	List<GrantedAuthority> authorities = getAuthorities();
	if (authorities.isEmpty()) {
		throw new UsernameNotFoundException("User '" + username + "' has no roles.");
	}

	return new User(userDto.getEmail(),
		userDto.getPassword(),
		userDto.isActive(),
		true,
		true,
		true,
		authorities);
  }

  private List<GrantedAuthority> getAuthorities() {
	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ADM"));
	return authorities;
  }
}
