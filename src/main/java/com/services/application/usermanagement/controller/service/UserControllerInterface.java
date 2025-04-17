package com.services.application.usermanagement.controller.service;

import com.services.application.usermanagement.model.api.UserSaveRequest;
import com.services.application.usermanagement.model.api.UserResponse;
import com.services.application.usermanagement.model.api.UserUpdateRequest;
import com.services.application.usermanagement.model.error.ErrorResponse;
import com.services.application.usermanagement.util.Constants;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public interface UserControllerInterface {

  @Operation(summary = "Get all users", description = "Retrieve a list of all users")
  @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserResponse.class)))
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  Maybe<ResponseEntity<Flowable<UserResponse>>> getAllUsers();

  @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
  @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponse.class)))
  @ApiResponse(responseCode = "404", description = Constants.Message.ERROR_USER_NOT_FOUND,
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)))
  @GetMapping(value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
  Maybe<ResponseEntity<UserResponse>> getUserById(String id);

@Operation(summary = "Create a new user", description = "Add a new user to the system")
@ApiResponse(responseCode = "201", description = "User created successfully",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UserResponse.class)))
@ApiResponse(responseCode = "404", description = Constants.Message.ERROR_PASSWORD_CRITERIA,
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(responseCode = "404", description = Constants.Message.ERROR_EMAIL_ALREADY_EXISTS,
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(responseCode = "500", description = Constants.Message.ERROR_GENERIC,
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = ErrorResponse.class)))
@PostMapping(value = "/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
  Maybe<ResponseEntity<UserResponse>> createUser(UserSaveRequest userSaveRequest);

  @Operation(summary = "Update an existing user", description = "Update user details by ID")
  @ApiResponse(responseCode = "200", description = "User updated successfully",
          content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = UserResponse.class)))
  @ApiResponse(responseCode = "404", description = Constants.Message.ERROR_PASSWORD_CRITERIA,
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = Constants.Message.ERROR_EMAIL_ALREADY_EXISTS,
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "500", description = Constants.Message.ERROR_GENERIC,
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ErrorResponse.class)))
  @PutMapping(value = "/{id}",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  Maybe<ResponseEntity<UserResponse>> updateUser(String id, UserUpdateRequest userUpdateRequest);

  @Operation(summary = "Delete a user", description = "Remove a user by their ID")
  @ApiResponse(responseCode = "204", description = "User deleted successfully")
  @ApiResponse(responseCode = "404", description = Constants.Message.ERROR_USER_NOT_FOUND,
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ErrorResponse.class)))
  @DeleteMapping("/{id}")
  Maybe<ResponseEntity<Void>> deleteUser(String id);
}
