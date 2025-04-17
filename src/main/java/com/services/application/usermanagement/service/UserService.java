package com.services.application.usermanagement.service;

import com.services.application.usermanagement.model.api.UserResponse;
import com.services.application.usermanagement.model.dto.UserDto;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface UserService {

  Maybe<UserResponse> createUser(UserDto userDto);

  Maybe<UserResponse> getUserById(String id);

  Flowable<UserResponse> getAllUsers();

  Maybe<UserResponse> updateUser(UserDto userDto);

  Completable deleteUser(String id);

  UserDto findByEmail(String email);

  void updateLastLogin(String id);
}