package com.services.application.usermanagement.config;

import com.services.application.usermanagement.exception.CustomException;
import com.services.application.usermanagement.model.error.ErrorResponse;
import com.services.application.usermanagement.util.Constants;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<Object> handleCustomException(CustomException ex) {
    return ResponseEntity
        .status(ex.getStatus())
        .body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(ex.getMessage() != null ? ex
            .getMessage() : Constants.Message.ERROR_GENERIC));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    String firstErrorMessage = ex.getBindingResult().getFieldErrors()
          .stream()
          .findFirst()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .orElse("Validation error");

    return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(firstErrorMessage));
  }
}