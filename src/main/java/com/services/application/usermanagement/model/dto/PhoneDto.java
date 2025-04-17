package com.services.application.usermanagement.model.dto;

import lombok.*;

@Data
public class PhoneDto {

  private Long id;
  private String number;
  private String cityCode;
  private String countryCode;
}
