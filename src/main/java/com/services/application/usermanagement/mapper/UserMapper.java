package com.services.application.usermanagement.mapper;

import com.services.application.usermanagement.model.api.UserSaveRequest;
import com.services.application.usermanagement.model.api.UserResponse;
import com.services.application.usermanagement.model.api.UserUpdateRequest;
import com.services.application.usermanagement.model.dto.UserDto;
import com.services.application.usermanagement.model.entity.User;

public interface UserMapper {

  UserDto mapUserSaveRequestToUserDto(UserSaveRequest userSaveRequest);

  UserDto mapUserUpdateRequestToUserDto(UserUpdateRequest userUpdateRequest);

  User mapUserDtoToUser(UserDto userDto);

  UserDto mapUserToUserDto(User user);

  UserResponse mapUserToUserResponse(User user);
}