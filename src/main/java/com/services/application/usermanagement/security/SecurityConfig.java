package com.services.application.usermanagement.security;

import com.services.application.usermanagement.security.service.JWTService;
import com.services.application.usermanagement.security.service.impl.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private UserDetailServiceImpl userDetailServiceImpl;

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  private JWTService jwtService;

  private static final String[] AUTH_WHITELIST = {
			"/v3/api-docs/**",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui.html",
			"/webjars/**",
			"/swagger-ui/**"
  };

  @Override
  protected void configure(HttpSecurity http) throws Exception{
	http.cors().and()
	.authorizeRequests()
	.antMatchers(AUTH_WHITELIST).permitAll()
	.anyRequest().authenticated()
	.and()
	.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService))
	.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtService))
	.csrf().disable()
	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Autowired
  public void configureGlobalAuthentication(AuthenticationManagerBuilder authManagerBuilder)
		  throws Exception {
	authManagerBuilder.userDetailsService(userDetailServiceImpl)
			.passwordEncoder(bCryptPasswordEncoder);
  }
}
