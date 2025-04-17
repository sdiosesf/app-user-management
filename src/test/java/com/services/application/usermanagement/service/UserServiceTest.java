package com.services.application.usermanagement.service;

import static org.mockito.Mockito.*;

import com.services.application.usermanagement.exception.CustomException;
import com.services.application.usermanagement.mapper.UserMapper;
import com.services.application.usermanagement.model.entity.User;
import com.services.application.usermanagement.repository.PhoneRepository;
import com.services.application.usermanagement.repository.UserRepository;
import com.services.application.usermanagement.service.impl.UserServiceImpl;
import com.services.application.usermanagement.util.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PhoneRepository phoneRepository;

  @Spy
  private UserMapper userMapper =
            Mappers.getMapper(UserMapper.class);

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  @DisplayName("return successful when get all users")
  void returnSuccessfulWhenGetAllUsers() {
    when(userRepository.findAll()).thenReturn(List.of(createSampleUser()));

    var testObserver = userService.getAllUsers().test();

    testObserver.awaitTerminalEvent();
    testObserver.assertNoErrors()
            .assertComplete();
  }

  @Test
  @DisplayName("return successful when get user by id")
  void returnSuccessfulWhenGetUserById() {
    String userId = "f9eeefe6-4c44-4f21-8f9f-118d8be37762";
    when(userRepository.findById(userId)).thenReturn(Optional.of(createSampleUser()));

    var testObserver = userService.getUserById(userId).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertNoErrors()
            .assertComplete();
  }

  @Test
  @DisplayName("return error when get user by id")
  void returnErrorWhenGetUserById() {
    String userId = "f9eeefe6-4c44-4f21-8f9f-118d8be37762";
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    var testObserver = userService.getUserById(userId).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertError(throwable -> throwable instanceof CustomException &&
            Constants.Message.ERROR_USER_NOT_FOUND.equals(throwable.getMessage()))
            .assertNotComplete();
  }

  @Test
  @DisplayName("return successful when delete by id")
  void returnSuccessfulWhenDeleteById() {
    String userId = "f9eeefe6-4c44-4f21-8f9f-118d8be37762";

    when(userRepository.findById(userId)).thenReturn(Optional.of(createSampleUser()));
    doNothing().when(phoneRepository).deleteByUserId(any());
    doNothing().when(userRepository).deleteById(any());

    var testObserver = userService.deleteUser(userId).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertNoErrors()
          .assertComplete();
  }

  @Test
  @DisplayName("return error when delete by id")
  void returnErrorWhenDeleteById() {
    String userId = "f9eeefe6-4c44-4f21-8f9f-118d8be37762";

    when(userRepository.findById(any())).thenReturn(Optional.empty());

    var testObserver = userService.deleteUser(userId).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertError(throwable -> throwable instanceof CustomException &&
            Constants.Message.ERROR_USER_NOT_FOUND.equals(throwable.getMessage()))
            .assertNotComplete();
  }

  private User createSampleUser() {
    User user = new User();
    user.setId("f9eeefe6-4c44-4f21-8f9f-118d8be37762");
    user.setName("John Doe");
    user.setEmail("johndoe@example.com");
    user.setActive(true);
    return user;
  }
}
