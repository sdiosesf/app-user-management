package com.services.application.usermanagement.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public String encryptPassword(String password) {
    return passwordEncoder.encode(password);
  }

  public boolean matches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}