package com.services.application.usermanagement.model.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PhoneUpdateRequest {

  private Long id;

  @Size(min = 7, max = 9)
  @Pattern(regexp = "^[0-9]+$")
  private String number;

  @NotBlank
  @Size(min = 7, max = 9)
  @Pattern(regexp = "^[0-9]+$")
  private String cityCode;

  @NotBlank
  @Size(max = 3)
  @Pattern(regexp = "^[0-9]+$")
  private String countryCode;
}
