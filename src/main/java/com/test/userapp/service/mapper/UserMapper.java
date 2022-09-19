package com.test.userapp.service.mapper;

import com.test.userapp.dto.request.UserCreateRequestDto;
import com.test.userapp.dto.request.UserPatchRequestDto;
import com.test.userapp.dto.response.UserResponseDto;
import com.test.userapp.entity.User;

public interface UserMapper {
    User fromDto(UserCreateRequestDto userDto);
    User fromDto(UserPatchRequestDto userDto);
    UserResponseDto toDto(User user);
}
