package com.services.application.usermanagement.service.impl;

import com.services.application.usermanagement.exception.CustomException;
import com.services.application.usermanagement.mapper.UserMapper;
import com.services.application.usermanagement.model.api.UserResponse;
import com.services.application.usermanagement.model.dto.UserDto;
import com.services.application.usermanagement.model.entity.Phone;
import com.services.application.usermanagement.repository.PhoneRepository;
import com.services.application.usermanagement.repository.UserRepository;
import com.services.application.usermanagement.service.UserService;
import com.services.application.usermanagement.util.Constants;
import com.services.application.usermanagement.util.PasswordUtil;
import com.services.application.usermanagement.util.Util;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import com.services.application.usermanagement.model.entity.User;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PhoneRepository phoneRepository;

  private final PasswordUtil passwordUtil;

  private final UserMapper userMapper;

  @Value("${password.regex}")
  private final String passwordRegex;

  public UserServiceImpl(UserRepository userRepository,
        PhoneRepository phoneRepository,
        PasswordUtil passwordUtil,
        UserMapper userMapper,
        @Value("${password.regex}")
        String passwordRegex) {
    this.userRepository = userRepository;
    this.phoneRepository = phoneRepository;
    this.passwordUtil = passwordUtil;
    this.passwordRegex = passwordRegex;
    this.userMapper = userMapper;
  }

  @Override
  @Transactional
  public Maybe<UserResponse> createUser(UserDto userDto) {
    return validateUserRequest(userDto, Boolean.TRUE)
        .flatMap(userSaveDto -> {
          userSaveDto.setPassword(passwordUtil.encryptPassword(userSaveDto.getPassword()));

          User userAux = userMapper.mapUserDtoToUser(userSaveDto);
          User savedUser = userRepository.save(userAux);

          List<Phone> updatedPhones = savePhonesForUser(savedUser, userAux.getPhones());
          savedUser.setPhones(updatedPhones);

          return Maybe.just(userMapper.mapUserToUserResponse(savedUser));
        });
  }

  private List<Phone> savePhonesForUser(User user, List<Phone> phones) {
    if (phones == null) {
      return Collections.emptyList();
    }
    return phones.stream()
        .map(phone -> {
          phone.setUser(user);
          return phoneRepository.save(phone);
        })
        .collect(Collectors.toList());
  }

  private Maybe<UserDto> validateUserRequest(UserDto userDto, boolean isNewUser) {
    if (!userDto.getPassword().matches(passwordRegex)) {
      return Maybe.error(new CustomException(Constants.Message
              .ERROR_PASSWORD_CRITERIA, HttpStatus.BAD_REQUEST));
    }

    if (isNewUser && userRepository.findByEmailAndIsActiveTrue(userDto.getEmail()) != null) {
      return Maybe.error(new CustomException(Constants.Message
              .ERROR_EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST));
    }

    if (!isNewUser && userRepository.findByEmailAndIdNotAndIsActiveTrue(userDto
        .getEmail(), userDto.getId()) != null) {
      return Maybe.error(new CustomException(Constants.Message
              .ERROR_EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST));
    }

    return Maybe.just(userDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Maybe<UserResponse> getUserById(String id) {
    return Maybe.defer(() -> userRepository.findById(id)
        .map(userMapper::mapUserToUserResponse)
        .map(Maybe::just)
        .orElseGet(Maybe::empty))
        .switchIfEmpty(Maybe.error(new CustomException(Constants
            .Message.ERROR_USER_NOT_FOUND, HttpStatus.NOT_FOUND)));
  }

  @Override
  @Transactional(readOnly = true)
  public Flowable<UserResponse> getAllUsers() {
    return Flowable.defer(() -> Flowable.fromIterable(userRepository.findAll()
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList())))
        .subscribeOn(Schedulers.io());
  }

  @Override
  @Transactional
  public Maybe<UserResponse> updateUser(UserDto userDto) {
    return validateUserRequest(userDto, Boolean.FALSE)
        .flatMap(userAuxDto -> Maybe.defer(() -> userRepository.findById(userAuxDto.getId())
              .map(existingUser -> updateExistingUser(existingUser, userDto))
              .map(Maybe::just)
              .orElseGet(Maybe::empty)))
        .switchIfEmpty(Maybe.error(new CustomException(Constants.Message
              .ERROR_USER_NOT_FOUND, HttpStatus.NOT_FOUND)));
  }

  private UserResponse updateExistingUser(User existingUser, UserDto userDto) {
    userDto.setPassword(passwordUtil.encryptPassword(userDto.getPassword()));
    userDto.setModified(Util.getCurrentDateTime());
    userDto.setCreated(existingUser.getCreated());
    userDto.setLastLogin(existingUser.getLastLogin());

    User userAux = userMapper.mapUserDtoToUser(userDto);
    User updateUser = userRepository.save(userAux);

    List<Phone> updatedPhones = savePhonesForUser(updateUser, userAux.getPhones());
    updateUser.setPhones(updatedPhones);

    return userMapper.mapUserToUserResponse(updateUser);
  }

  @Override
  @Transactional
  public Completable deleteUser(String id) {
    return Completable.fromAction(() -> {
          userRepository.findById(id).orElseThrow(() ->
              new CustomException(Constants.Message.ERROR_USER_NOT_FOUND, HttpStatus.NOT_FOUND));
          phoneRepository.deleteByUserId(id);
          userRepository.deleteById(id);
      });
  }

  @Override
  @Transactional
  public UserDto findByEmail(String email) {
    User user = userRepository.findByEmailAndIsActiveTrue(email);
    if (user == null) {
        throw new CustomException(Constants.Message.ERROR_USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return userMapper.mapUserToUserDto(user);
  }

  @Override
  @Transactional
  public void updateLastLogin(String id) {
    User user = userRepository.findById(id).orElseThrow(() ->
            new CustomException(Constants.Message.ERROR_USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    user.setLastLogin(Util.getCurrentDateTime());
    userRepository.save(user);
  }
}