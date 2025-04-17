package com.services.application.usermanagement.controller;

import com.services.application.usermanagement.controller.service.UserControllerInterface;
import com.services.application.usermanagement.mapper.UserMapper;
import com.services.application.usermanagement.model.api.UserSaveRequest;
import com.services.application.usermanagement.model.api.UserResponse;
import com.services.application.usermanagement.model.api.UserUpdateRequest;
import com.services.application.usermanagement.service.UserService;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController implements UserControllerInterface {

  private UserService userService;

  private UserMapper userMapper;

  @Override
  public Maybe<ResponseEntity<Flowable<UserResponse>>> getAllUsers() {
    return Maybe.just(ResponseEntity.ok(userService.getAllUsers()));
  }

  @Override
  public Maybe<ResponseEntity<UserResponse>> getUserById(@PathVariable String id) {
    return userService.getUserById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  public Maybe<ResponseEntity<UserResponse>> createUser(@Valid
      @RequestBody UserSaveRequest userSaveRequest) {
    return userService.createUser(userMapper.mapUserSaveRequestToUserDto(userSaveRequest))
        .map(ResponseEntity::ok);
  }

  @Override
  public Maybe<ResponseEntity<UserResponse>> updateUser(@PathVariable String id,
      @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
    return userService.updateUser(userMapper.mapUserUpdateRequestToUserDto(userUpdateRequest))
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Override
  public Maybe<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
    return userService.deleteUser(id)
        .andThen(Maybe.just(ResponseEntity.noContent().build()));
  }
}