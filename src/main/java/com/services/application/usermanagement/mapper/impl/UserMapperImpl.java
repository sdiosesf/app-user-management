package com.services.application.usermanagement.mapper.impl;

import com.services.application.usermanagement.mapper.UserMapper;
import com.services.application.usermanagement.model.api.*;
import com.services.application.usermanagement.model.dto.PhoneDto;
import com.services.application.usermanagement.model.dto.UserDto;
import com.services.application.usermanagement.model.entity.Phone;
import com.services.application.usermanagement.model.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto mapUserSaveRequestToUserDto(UserSaveRequest userSaveRequest) {
        if (userSaveRequest == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setName(userSaveRequest.getName());
        userDto.setEmail(userSaveRequest.getEmail());
        userDto.setPassword(userSaveRequest.getPassword());
        userDto.setPhones(mapPhoneRequestListToPhoneDtoList(userSaveRequest.getPhones()));
        userDto.setActive(userSaveRequest.isActive());

        return userDto;
    }

    @Override
    public UserDto mapUserUpdateRequestToUserDto(UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(userUpdateRequest.getId());
        userDto.setName(userUpdateRequest.getName());
        userDto.setEmail(userUpdateRequest.getEmail());
        userDto.setPassword(userUpdateRequest.getPassword());
        userDto.setPhones(mapPhoneRequestListToPhoneDtoList(userUpdateRequest.getPhones()));
        userDto.setActive(userUpdateRequest.isActive());

        return userDto;
    }

    @Override
    public User mapUserDtoToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setCreated(userDto.getCreated());
        user.setModified(userDto.getModified());
        user.setLastLogin(userDto.getLastLogin());
        user.setActive(userDto.isActive());
        user.setPhones(mapPhoneDtoListToPhoneList(userDto.getPhones()));

        return user;
    }

    @Override
    public UserDto mapUserToUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPhones(mapPhoneListToPhoneDtoList(user.getPhones()));
        userDto.setCreated(user.getCreated());
        userDto.setModified(user.getModified());
        userDto.setLastLogin(user.getLastLogin());
        userDto.setActive(user.isActive());

        return userDto;
    }

    @Override
    public UserResponse mapUserToUserResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setPhones(mapPhoneListToPhoneResponseList(user.getPhones()));
        userResponse.setCreated(user.getCreated());
        userResponse.setModified(user.getModified());
        userResponse.setLastLogin(user.getLastLogin());
        userResponse.setActive(user.isActive());

        return userResponse;
    }

    private <T> List<PhoneDto> mapPhoneRequestListToPhoneDtoList(List<T> phoneRequests) {
        if (phoneRequests == null || phoneRequests.isEmpty()) {
            return Collections.emptyList();
        }

        return phoneRequests.stream()
                .map(request -> {
                    if (request instanceof PhoneSaveRequest) {
                        return mapPhoneSaveRequestToPhoneDto((PhoneSaveRequest) request);
                    } else if (request instanceof PhoneUpdateRequest) {
                        return mapPhoneUpdateRequestToPhoneDto((PhoneUpdateRequest) request);
                    } else {
                        throw new IllegalArgumentException("Unsupported request type: " + request.getClass().getName());
                    }
                })
                .collect(Collectors.toList());
    }

    private PhoneDto mapPhoneSaveRequestToPhoneDto(PhoneSaveRequest phoneSaveRequest) {
        if (phoneSaveRequest == null) {
            return null;
        }

        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setNumber(phoneSaveRequest.getNumber());
        phoneDto.setCityCode(phoneSaveRequest.getCityCode());
        phoneDto.setCountryCode(phoneSaveRequest.getCountryCode());
        return phoneDto;
    }

    private PhoneDto mapPhoneUpdateRequestToPhoneDto(PhoneUpdateRequest phoneUpdateRequest) {
        if (phoneUpdateRequest == null) {
            return null;
        }

        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setId(phoneUpdateRequest.getId());
        phoneDto.setNumber(phoneUpdateRequest.getNumber());
        phoneDto.setCityCode(phoneUpdateRequest.getCityCode());
        phoneDto.setCountryCode(phoneUpdateRequest.getCountryCode());
        return phoneDto;
    }

    private List<Phone> mapPhoneDtoListToPhoneList(List<PhoneDto> phonesDto) {
        if (phonesDto == null || phonesDto.isEmpty()) {
            return Collections.emptyList();
        }

        return phonesDto.stream()
                .map(phoneDto -> {
                    Phone phone = new Phone();
                    phone.setId(phoneDto.getId());
                    phone.setNumber(phoneDto.getNumber());
                    phone.setCityCode(phoneDto.getCityCode());
                    phone.setCountryCode(phoneDto.getCountryCode());
                    return phone;
                })
                .collect(Collectors.toList());
    }

    private List<PhoneDto> mapPhoneListToPhoneDtoList(List<Phone> phones) {
        if (phones == null || phones.isEmpty()) {
            return Collections.emptyList();
        }

        return phones.stream()
                .map(phone -> {
                    PhoneDto phoneDto = new PhoneDto();
                    phoneDto.setId(phone.getId());
                    phoneDto.setNumber(phone.getNumber());
                    phoneDto.setCityCode(phone.getCityCode());
                    phoneDto.setCountryCode(phone.getCountryCode());
                    return phoneDto;
                })
                .collect(Collectors.toList());
    }

    private List<PhoneResponse> mapPhoneListToPhoneResponseList(List<Phone> phones) {
        if (phones == null || phones.isEmpty()) {
            return Collections.emptyList();
        }

        return phones.stream()
                .map(phone -> {
                    PhoneResponse phoneResponse = new PhoneResponse();
                    phoneResponse.setId(phone.getId());
                    phoneResponse.setNumber(phone.getNumber());
                    phoneResponse.setCityCode(phone.getCityCode());
                    phoneResponse.setCountryCode(phone.getCountryCode());
                    return phoneResponse;
                })
                .collect(Collectors.toList());
    }
}