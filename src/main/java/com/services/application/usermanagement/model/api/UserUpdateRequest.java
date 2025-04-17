package com.services.application.usermanagement.model.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserUpdateRequest {

  private String id;

  @NotBlank(message = "Name cannot be blank")
  @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
  private String name;

  @NotBlank(message = "Email cannot be blank")
  @Size(min = 1, max = 70, message = "Email must be between 1 and 70 characters")
  @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]+$", message = "Email must be a valid email address")
  private String email;

  @NotBlank(message = "Password cannot be blank")
  @Size(min = 1, max = 20, message = "Password must be between 1 and 20 characters")
  private String password;

  private List<PhoneUpdateRequest> phones;

  private boolean isActive;
}
