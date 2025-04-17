package com.services.application.usermanagement.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

  private final String message;
}