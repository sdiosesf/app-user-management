package com.services.application.usermanagement.model.api;

import lombok.*;

import java.util.List;

@Data
public class UserResponse {

  private String id;
  private String name;
  private String email;
  private String password;
  private List<PhoneResponse> phones;
  private String created;
  private String modified;
  private String lastLogin;
  private boolean isActive;
}
