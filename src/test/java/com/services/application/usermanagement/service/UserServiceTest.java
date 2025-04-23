package com.services.application.usermanagement.service;

import static org.mockito.Mockito.*;

import com.services.application.usermanagement.exception.CustomException;
import com.services.application.usermanagement.mapper.UserMapper;
import com.services.application.usermanagement.mapper.impl.UserMapperImpl;
import com.services.application.usermanagement.model.dto.PhoneDto;
import com.services.application.usermanagement.model.dto.UserDto;
import com.services.application.usermanagement.model.entity.Phone;
import com.services.application.usermanagement.model.entity.User;
import com.services.application.usermanagement.repository.PhoneRepository;
import com.services.application.usermanagement.repository.UserRepository;
import com.services.application.usermanagement.service.impl.UserServiceImpl;
import com.services.application.usermanagement.util.Constants;
import com.services.application.usermanagement.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

  @Mock
  private PasswordUtil passwordUtil;

  @Spy
  private UserMapper userMapper = new UserMapperImpl();

  @InjectMocks
  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    String passwordRegex = "^.{6,}$";
    userService = new UserServiceImpl(userRepository, phoneRepository, passwordUtil, userMapper, passwordRegex);
  }

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

  @Test
  @DisplayName("return error password criteria when create user")
  void returnErrorPasswordCriteriaWhenCreateUser() {
    UserDto userdto = createSampleUserdto();
    userdto.setPassword("123");

    var testObserver = userService.createUser(userdto).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertError(throwable -> throwable instanceof CustomException &&
            Constants.Message.ERROR_PASSWORD_CRITERIA.equals(throwable.getMessage()))
            .assertNotComplete();
  }

  @Test
  @DisplayName("return error email already exists when create user")
  void returnErrorEmailAlreadyExistsWhenCreateUser() {
    UserDto userdto = createSampleUserdto();
    userdto.setPassword("Impka1234$");

    when(userRepository.findByEmailAndIsActiveTrue(any())).thenReturn(createSampleUser());

    var testObserver = userService.createUser(userdto).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertError(throwable -> throwable instanceof CustomException &&
            Constants.Message.ERROR_EMAIL_ALREADY_EXISTS.equals(throwable.getMessage()))
            .assertNotComplete();
  }

  @Test
  @DisplayName("return successful when create user")
  void returnSuccessfulWhenCreateUser() {
    UserDto userdto = createSampleUserdto();
    userdto.setPassword("Impka1234$");

    when(userRepository.findByEmailAndIsActiveTrue(any())).thenReturn(null);

    when(passwordUtil.encryptPassword(any())).thenReturn("$2a$10$AkPVjkiTcd/ZQx9avzVL7aBtq");

    when(userRepository.save(any())).thenReturn(createSampleUser());

    when(phoneRepository.save(any())).thenReturn(createSamplePhone());

    var testObserver = userService.createUser(userdto).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertNoErrors()
            .assertComplete();
  }

  @Test
  @DisplayName("return successful when update user")
  void returnSuccessfulWhenUpdateUser() {
    UserDto userdto = createSampleUserdto();
    userdto.setPassword("Impka1234$");

    when(userRepository.findById(any())).thenReturn(Optional.of(createSampleUser()));

    when(userRepository.findByEmailAndIdNotAndIsActiveTrue(any(), any())).thenReturn(null);

    when(passwordUtil.encryptPassword(any())).thenReturn("$2a$10$AkPVjkiTcd/ZQx9avzVL7aBtq");

    when(userRepository.save(any())).thenReturn(createSampleUser());

    when(phoneRepository.save(any())).thenReturn(createSamplePhone());

    var testObserver = userService.updateUser(userdto).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertNoErrors()
            .assertComplete();
  }

  private UserDto createSampleUserdto() {
    UserDto userDto = new UserDto();
    userDto.setName("John Doe");
    userDto.setEmail("johndoe@example.com");
    userDto.setPassword("ValidPassword123");
    userDto.setPhones(List.of(createSamplePhonedto()));
    return userDto;
  }

  private PhoneDto createSamplePhonedto() {
    PhoneDto phoneDto = new PhoneDto();
    phoneDto.setId(1L);
    phoneDto.setNumber("123456789");
    return phoneDto;
  }

  private User createSampleUser() {
    User user = new User();
    user.setId("f9eeefe6-4c44-4f21-8f9f-118d8be37762");
    user.setName("John Doe");
    user.setEmail("johndoe@example.com");
    user.setActive(true);
    user.setPhones(List.of(createSamplePhone()));
    return user;
  }

  private Phone createSamplePhone() {
    Phone phone = new Phone();
    phone.setId(1L);
    phone.setNumber("123456789");
    phone.setUser(new User());
    return phone;
  }
}
