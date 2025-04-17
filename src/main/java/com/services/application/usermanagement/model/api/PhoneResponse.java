package com.services.application.usermanagement.model.api;

import lombok.*;

@Data
public class PhoneResponse {

  private Long id;
  private String number;
  private String cityCode;
  private String countryCode;
}
